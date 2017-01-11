package com.toyfactory.pcb.service;

import java.util.ArrayList;
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
import com.toyfactory.pcb.domain.GamePatchLogPK;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.PcbGame;
import com.toyfactory.pcb.model.PcbGamePatch;
import com.toyfactory.pcb.model.PcbGamePatchResult;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.model.YN;
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
	
	public List<PcbGamePatchResult> buildPcbGamePathResultForPcbang(List<Pcbang> pcbangs, List<Game> games) {
		//최종 결과 
		//gamePatchMapForPcbang = { pcbId: 123, games:{{key(gsn1):value(설치여부)},{key(gsn2):value(설치여부),...}, ...}			
		List<PcbGamePatchResult> pcbGamePatchResultList = new ArrayList<PcbGamePatchResult>();
						
		for (Pcbang pcbang : pcbangs) {
			
			if (logger.isDebugEnabled()) logger.debug("pcbGamePatchResult pcb_id:" + pcbang.getPcbId());
			
			PcbGamePatchResult pcbGamePatchResult = new PcbGamePatchResult(pcbang);
			
			List<GamePatchLog> gamePatchLogs = gamePatchLogDao.findByPcbId(pcbang.getPcbId());
			
			pcbGamePatchResult.buildResult(gamePatchLogs, games);

			YN isPaymentPcbang = isMissionCompletePcbang(pcbang, games) ? YN.Y : YN.N;
			
			pcbGamePatchResult.setIsPaymentPcbang(isPaymentPcbang);
			
			pcbGamePatchResultList.add(pcbGamePatchResult);
		}
		
		return pcbGamePatchResultList;
	}
	
	/**
	 * redis에 저장된 PC방 별 데이터를 취합하여 GamePatchLog table에 데이터를 넣는 batch작업
	 * executeService를 통해서 실행해야 하고 중복으로 실행이 되지 않도록 해야 한다.
	 * @return
	 */
	public void excuteGamePatchAnalysisBatch() {
		
		//Pcbang별 IP 목록 별 PcbGamePatch 추출
		List<Pcbang> validPcbangs = pcbangService.findPcbangs("status", StatusCd.OK.toString());
		for (Pcbang pcbang : validPcbangs) {
			
			List<String> pcbangIPs = pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
			
			for (String ip : pcbangIPs) {
				PcbGamePatch pcbGamePatch = readPcbGamePatchFromCache(ip);
				if (pcbGamePatch == null) continue;
				
				processFromPcbGamePatchToGamePatchLog(pcbang.getPcbId(), pcbGamePatch);
			}			
		}		
	}
	
	public boolean writePcbGamePatchToCache(String clientIp, PcbGamePatch pcbGamePatch) {
    	pcbGamePatch.setCrtDt(new Date());
        redisTemplate.opsForValue().set(clientIp, pcbGamePatch);		
		return true;
	}
	
	public PcbGamePatch readPcbGamePatchFromCache(String clientIp) {
		return redisTemplate.opsForValue().get(clientIp);
	}
	
	public boolean processFromPcbGamePatchToGamePatchLog(Long pcbId, PcbGamePatch pcbGamePatch) {		
		List<PcbGame> pcbGames = pcbGamePatch.getPcbGames();

		if (pcbGames.isEmpty()) return false;
		
		for (PcbGame pcbGame : pcbGames) {
			//GSN별 기존 데이터 저장된 데이터 검색 없는 경우 새로 생성			
			GamePatchLog gamePatchLog = gamePatchLogDao.findOne(new GamePatchLogPK(pcbId, pcbGame.getGsn()));
			
			if (gamePatchLog == null) {
				gamePatchLog = new GamePatchLog(pcbId, pcbGame.getGsn());				
			} 
			
			//GSN별 PATCH 여부 체크
			if (verifyPcbGamePatch(pcbGame)) {
				gamePatchLog.incrPatch();
			}
			
			gamePatchLog.incrInstall();
			
			gamePatchLogDao.save(gamePatchLog);
		}
		
		return true;
	}
	
	public boolean verifyPcbGamePatch(PcbGame pcbGame) {
		
		Game game = gameService.findGame(pcbGame.getGsn());
				
		//TODO: Game 별로 다른 검증 방법을 사용하는 것을 지원해야 한다.
		if (!game.getMajor().equals(pcbGame.getMajor())) {
			return false;
		}

		return true;
	}
	
	public boolean isMissionCompletePcbang(Pcbang pcbang, List<Game> games) {
		//현재 지급대상 PC방은 모든 게임의 patch 수가  전체 IP 수의 50% 이상이어야 한다.
		
		//List<Game> games = gameService.findGames();
				
		for (Game aGame : games) {
			GamePatchLog gamePatchLog = gamePatchLogDao.findOne(new GamePatchLogPK(pcbang.getPcbId(), aGame.getGsn()));
			if (gamePatchLog == null) {
				return false;
			}
			
			// 50% 미만은 지급 대상이 아니다.
			if ((gamePatchLog.getPatch() * 2L) < pcbang.getIpTotal()) {
				return false;
			}			
		}
		
		return true;
	}
	
	public String checkGamePatchPass(String clientIp) {
    	//PcbAgent가 중복해서 GamePatch check 하는 것을 방지하기 위한 사전 체크 
    	
    	if ("127.0.0.1".equals(clientIp)) {
    		return "PASS"; //do nothing
    	}
    	
    	PcbGamePatch pcbGamePatch = readPcbGamePatchFromCache(clientIp);
    	
    	if (pcbGamePatch == null) {
    		return "CHECK";
    	}

    	Date now = new Date();
    	//마지막으로 저장이 된지 24시간이 지나지 않았으면  pass    	
    	if ((now.getTime() - pcbGamePatch.getCrtDt().getTime())/1000 < 86400L) {
    		
    		if(logger.isDebugEnabled()){
    			logger.debug("[checkGamePatchPass] PASS! client ip:" + clientIp);
    		}    		
    		return "PASS";
    	}
		
		return "CHECK";
	}
}
