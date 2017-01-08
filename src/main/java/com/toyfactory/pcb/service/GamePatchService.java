package com.toyfactory.pcb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.PcbGame;
import com.toyfactory.pcb.model.PcbGamePatch;
import com.toyfactory.pcb.model.PcbGamePatchResult;
import com.toyfactory.pcb.repository.GamePatchLogRepository;
import com.toyfactory.pcb.repository.GameRepository;
import com.toyfactory.pcb.repository.PcbangRepository;


@Service("gamePatchService")
public class GamePatchService {

	private static final Logger logger = LoggerFactory.getLogger(GamePatchService.class);	
	
	private static long EXPIRE_TERM = 43200L; //24hours
	
	@Autowired
	private GamePatchLogRepository gamePatchLogDao;
	
	@Autowired	
	private GameService gameService;
	
	@Autowired	
	private PcbangRepository pcbangDao;

	@Autowired	
	private PcbangService pcbangService;	
	
    @Autowired @Qualifier("jsonRedisTemplate")
    private RedisTemplate<String, PcbGamePatch> redisTemplate;	
	
	public Map<Long, PcbGamePatchResult> buildGamePathForAllPcbang() {
		//최종 결과 
		//gamePatchMapForPcbang = { pcbId: 123, games:{{key(gsn1):value(설치여부)},{key(gsn2):value(설치여부),...}, ...}			
		Map<Long, PcbGamePatchResult> gamePatchMapForPcbang = new HashMap<Long, PcbGamePatchResult>();
		
		Map<String, Game> gamesMap = gameService.buildAllGamesMap();
			
		List<GamePatchLog> gamePatchLogs = gamePatchLogDao.findAll();
		
		for(GamePatchLog gamePatchLog : gamePatchLogs) {
			PcbGamePatchResult gamePatchResult = gamePatchMapForPcbang.get(gamePatchLog.getPcbId());
			
			if(gamePatchResult == null){
				gamePatchResult = new PcbGamePatchResult();
				gamePatchMapForPcbang.put(gamePatchLog.getPcbId(), gamePatchResult);
			}
			
			gamePatchResult.verifyGamePatch(gamePatchLog, gamesMap.get(gamePatchLog.getGsn()));
		}

		//status = OK 인 모든 pc방에 대해서 game patch 여부 조사
		List<Pcbang> pcbangs = pcbangService.findPcbangs("status", "OK");	
		
		for(Pcbang pcbang : pcbangs) {
			PcbGamePatchResult gamePatchResult = gamePatchMapForPcbang.get(pcbang.getPcbId());
			
			//수집된 gamePatchLog 보다 pcbang 수가 많은 경우 빈 데이터를 넣어 준다.
			if(gamePatchResult == null) {
				gamePatchResult = new PcbGamePatchResult();			
				gamePatchMapForPcbang.put(pcbang.getPcbId(), gamePatchResult);
			}
			
			gamePatchResult.verifyAllGamePatch(gamesMap.size());
		}
		
		return gamePatchMapForPcbang;
	}
	
	/**
	 * redis에 저장된 PC방 별 데이터를 취합하여 GamePatchLog table에 데이터를 넣는 batch작업
	 * executeService를 통해서 실행해야 하고 중복으로 실행이 되지 않도록 해야 한다.
	 * @return
	 */
	public void excuteGamePatchAnalysisBatch(){
		
		//Pcbang별 IP 목록 별 PcbGamePatch 추출
		List<Pcbang> vaildPcbangs = pcbangService.findPcbangs("status", "OK");
		for(Pcbang pcbang : vaildPcbangs) {
			
			List<String> pcbangIPs = pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
			
			for(String ip : pcbangIPs) {
				PcbGamePatch pcbGamePatch = readPcbGamePatchFromCache(ip);
				if(pcbGamePatch == null) continue;
				
				//한번이라도 저장에 성공을 하면 중단한다.
				if(savePcbGamePatchToGamePatchLog(pcbang.getPcbId(), pcbGamePatch)) break;
			}			
		}		
	}
	
	public boolean writePcbGamePatchToCache(String clientIp, PcbGamePatch pcbGamePatch){
    	pcbGamePatch.setCrtDt(new Date());
        redisTemplate.opsForValue().set(clientIp, pcbGamePatch);		
		return true;
	}
	
	public PcbGamePatch readPcbGamePatchFromCache(String clientIp){
		return redisTemplate.opsForValue().get(clientIp);
	}
	
	public boolean savePcbGamePatchToGamePatchLog(Long pcbId, PcbGamePatch pcbGamePatch){		
		List<PcbGame> pcbGames = pcbGamePatch.getPcbGames();

		if(pcbGames.isEmpty()) return false;
		
		for(PcbGame pcbGame : pcbGames){
			GamePatchLog gamePatchLog = new GamePatchLog(pcbId, pcbGame.getGsn(), pcbGame.getMajor(), pcbGame.getMinor());
			gamePatchLogDao.save(gamePatchLog);
		}
		
		return true;
	}
	
	public boolean isUpdatedAllGamePatchLog(Long pcbId, Date checkDt){
		//최신 gamepatch로 모두 업데이트 되어 있으면 더이상 업데이트 하지 않는다.
		//12시간 이전에 저장된 데이터가 있는지 조사한다.
		Date uptDt = new Date(checkDt.getTime()-EXPIRE_TERM);
		List<GamePatchLog> gamePatchLogs = gamePatchLogDao.findByPcbIdAndUptDt(pcbId, uptDt);
		
		if(gamePatchLogs.isEmpty()) return false;
		
		Map<String, Game> allGameMap =  gameService.buildAllGamesMap();
		
		return false;
	}
	
	public String checkGamePatchPass(String clientIp){
    	//PcbAgent가 중복해서 GamePatch check 하는 것을 방지하기 위한 사전 체크 
    	
    	if("127.0.0.1".equals(clientIp)){
    		return "PASS"; //do nothing
    	}
    	
    	PcbGamePatch pcbGamePatch = readPcbGamePatchFromCache(clientIp);
    	
    	if(pcbGamePatch == null){
    		return "CHECK";
    	}

    	Date now = new Date();
    	//마지막으로 저장이 된지 24시간이 지나지 않았으면  pass    	
    	if((now.getTime() - pcbGamePatch.getCrtDt().getTime())/1000 < 86400L) {
    		
    		if(logger.isDebugEnabled()){
    			logger.debug("[checkGamePatchPass] PASS! client ip:" + clientIp);
    		}    		
    		return "PASS";
    	}
		
		return "CHECK";
	}
}
