package com.toyfactory.pcb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.PcbGamePatchResult;
import com.toyfactory.pcb.repository.GamePatchLogRepository;
import com.toyfactory.pcb.repository.GameRepository;
import com.toyfactory.pcb.repository.PcbangRepository;


@Service("gamePatchService")
public class GamePatchService {

	@Autowired
	private GamePatchLogRepository gamePatchLogDao;
	
	@Autowired	
	private GameService gameService;
	
	@Autowired	
	private PcbangRepository pcbangDao;	
	
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

		//모든 pc방에 대해서 game patch 여부 조사
		List<Pcbang> pcbangs = pcbangDao.findAll();		
		
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
}
