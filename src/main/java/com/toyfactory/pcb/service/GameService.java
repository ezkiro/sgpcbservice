package com.toyfactory.pcb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.InstallPath;
import com.toyfactory.pcb.model.AgentCommand;
import com.toyfactory.pcb.model.GameCommand;
import com.toyfactory.pcb.repository.GameRepository;
import com.toyfactory.pcb.repository.InstallPathRepository;

@Service("gameService")
public class GameService {

	@Autowired	
	private GameRepository gameDao;
	
	@Autowired
	private InstallPathRepository installPathDao;

	public Map<String,Game> buildAllGamesMap() {
		
		List<Game> games = gameDao.findAll();
		
		Map<String,Game> gamesMap = new HashMap<String,Game>();
		
		for (Game game : games){
			gamesMap.put(game.getGsn(), game);
		}
		
		return gamesMap;
	}
	
	public List<Game> findGames() {
		//TODO: 다양한 검색 조건으로 검색
		return gameDao.findAll();
	}

	public List<Game> findEnableGames() {

		return gameDao.findByEnable("Y");
	}

	public Game findGame(String gsn){
		return gameDao.findOne(gsn);
	}
	
	@CacheEvict(cacheNames="agentCmdCache", allEntries=true)
	public boolean addGame(Game newGame) {
				
		Game existGame = gameDao.findOne(newGame.getGsn());
		
		if (existGame != null ) return false;

		gameDao.save(newGame);
		
		return true;
	}
	
	@CacheEvict(cacheNames="agentCmdCache", allEntries=true)	
	public boolean updateGame(Game existGame) {		
		existGame.setUptDt(new Date());
		
		gameDao.save(existGame);
		
		return true;
	}
	
	@CacheEvict(cacheNames="agentCmdCache", allEntries=true)	
	public void removeGame(String gsn) {
		gameDao.delete(gsn);
	}
	
	/**
	 * gsnList가 비어 있거나 null 이면 전체 리스트를 반환
	 * @param gsnList
	 * @return
	 */
	public List<Game> buildGames(String[] gsnList) {
	
		List<Game> games = new ArrayList<Game>();
		
		if (gsnList == null || gsnList.length == 0) {
			return games;
		}
				
		Map<String, Game> gamesMap = buildAllGamesMap();
		
		for (String gsn : gsnList) {
			games.add(gamesMap.get(gsn));
		}
				
		return games;
	}
	
	public List<InstallPath> findInstallPath(String gsn) {
		
		if (StringUtils.isEmpty(gsn)) {
			return installPathDao.findAllByOrderByGsn();
		}
		
		return installPathDao.findByGsn(gsn);
	}
	
	public InstallPath findInstallPathById(Long id) {
		return installPathDao.findOne(id);
	}
	
	@CacheEvict(cacheNames="agentCmdCache", allEntries=true)	
	public boolean updateInstallPath(InstallPath existPath) {		
		existPath.setUptDt(new Date());
		
		installPathDao.save(existPath);
		
		return true;
	}

	@CacheEvict(cacheNames="agentCmdCache", allEntries=true)	
	public InstallPath addInstallPath(InstallPath newPath) {
		
		return installPathDao.save(newPath);
	}
	
	@CacheEvict(cacheNames="agentCmdCache", allEntries=true)	
	public void removeInstallPath(Long id) {
		installPathDao.delete(id);
	}	
	
	@Cacheable(cacheNames="agentCmdCache", key="#command")
	public AgentCommand buildAgentCommand(String command) {
		
		AgentCommand agentCmd = new AgentCommand(command);
		agentCmd.setCmd(command);
		
		if ("PASS".equals(command)) {
			return agentCmd;
		}
		
		List<Game> games = findGames();
		
		for (Game game : games) {
			GameCommand gameCmd = new GameCommand(game);			
			gameCmd.setExpectedPaths(findInstallPath(game.getGsn()));
			
			agentCmd.getGameCommands().add(gameCmd);
		}
		
		return agentCmd;
	}
}
