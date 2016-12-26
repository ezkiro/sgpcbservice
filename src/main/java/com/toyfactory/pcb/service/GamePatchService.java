package com.toyfactory.pcb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.repository.GamePatchLogRepository;
import com.toyfactory.pcb.repository.GameRepository;
import com.toyfactory.pcb.repository.PcbangRepository;


@Service("gamePatchService")
public class GamePatchService {

	@Autowired
	GamePatchLogRepository gamePatchLogDao;
	
	@Autowired	
	GameRepository gameDao;
	
	@Autowired	
	private PcbangRepository pcbangDao;	
	
	public Map<Long, Map<String, String>> buildGamePathForAllPcbang() {
		//최종 결과 
		//gamePatchMapForPcbang = { pcbId: 123, games:{{key(gsn1):value(version1)},{key(gsn2):value(version2),...}, ...}			
		Map<Long, Map<String, String>> gamePatchMapForPcbang = new HashMap<Long, Map<String, String>>();
		
		//모든 pc방에 대해서 초기 데이터를 생성한다.
		List<Pcbang> pcbangs = pcbangDao.findAll();		
		List<Game> games = getAllGames();
		
		for(Pcbang pcbang : pcbangs) {	
			Map<String,String> gamePatchMap = new HashMap<String, String>();
			
			for(Game game : games) {
				gamePatchMap.put(game.getGsn(), "no version");
			}			
			gamePatchMapForPcbang.put(pcbang.getPcbId(), gamePatchMap);
		}
		
		List<GamePatchLog> gamePatchLogs = gamePatchLogDao.findAll();
		
		for(GamePatchLog item : gamePatchLogs) {
			Map<String,String> gamePatchMap = gamePatchMapForPcbang.get(item.getPcbId());			
			gamePatchMap.put(item.getGsn(), item.getMajor());
		}
		
		return gamePatchMapForPcbang;
	}	
	
	public List<Game> getAllGames() {
		return gameDao.findAll();
	}
	
}
