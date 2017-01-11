package com.toyfactory.pcb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.repository.GameRepository;

@Service("gameService")
public class GameService {

	@Autowired	
	private GameRepository gameDao;

	public Map<String,Game> buildAllGamesMap() {
		
		List<Game> games = gameDao.findAll();
		
		Map<String,Game> gamesMap = new HashMap<String,Game>();
		
		for(Game game : games){
			gamesMap.put(game.getGsn(), game);
		}
		
		return gamesMap;
	}
	
	public List<Game> findGames() {
		//TODO: 다양한 검색 조건으로 검색
		return gameDao.findAll();
	}

	public Game findGame(String gsn){
		return gameDao.findOne(gsn);
	}
	
	public boolean addGame(Game newGame) {
				
		Game existGame = gameDao.findOne(newGame.getGsn());
		
		if(existGame != null ) return false;

		gameDao.save(newGame);
		
		return true;
	}
	
	public boolean updateGame(Game existGame) {		
		existGame.setUptDt(new Date());
		
		gameDao.save(existGame);
		
		return true;
	}	
}
