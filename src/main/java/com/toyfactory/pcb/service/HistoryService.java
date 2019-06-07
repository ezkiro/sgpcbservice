package com.toyfactory.pcb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchHistory;
import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.History;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.repository.GamePatchHistoryRepository;
import com.toyfactory.pcb.repository.GamePatchLogRepository;
import com.toyfactory.pcb.repository.HistoryRepository;
import com.toyfactory.pcb.repository.PcbangRepository;

@Service("historyService")
public class HistoryService {

	private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);	
		
	@Autowired
	private GamePatchLogRepository gamePatchLogDao;
	
	@Autowired
	private GamePatchService gamePatchService;
	
	@Autowired	
	private PcbangRepository pcbangDao;
	
	@Autowired	
	private HistoryRepository historyDao;
	
	@Autowired
	private GamePatchHistoryRepository gamePatchHistoryDao;
	
	
	public void executeHistoryBatch(List<Game> games) {
		//반드시 excuteGamePatchAnalysisBatch() 이후에 실행이 되어야 한다.
		
		if (logger.isInfoEnabled()) {
			logger.info("start history batch");
		}
		
		List<Pcbang> pcbangs = pcbangDao.findByStatus(StatusCd.OK);
		//new History object

		//등록 PC방수
		Long pcbCnt = Long.valueOf(pcbangs.size());
		
		//지급  PC방수
		Long paidPcbCnt = pcbangs.stream().filter(pcbang -> gamePatchService.isMissionCompletePcbang(pcbang, games) == true).count();

		if (logger.isInfoEnabled()) {
			logger.info("all pcb count:" + pcbCnt + ", paid pcbCnt:" + paidPcbCnt);
		}
		
		//history 저장
		historyDao.save(new History(pcbCnt, paidPcbCnt, new Date()));

		Map<Long, Long> pcbangIpTotalMap = pcbangs.stream().collect(Collectors.toMap(Pcbang::getPcbId,Pcbang::getIpTotal));

		for(Game game : games) {

			List<GamePatchLog> gamePatchLogs = gamePatchLogDao.findByGsn(game.getGsn());

			//각 game별로  30% 이상 설치 달성한 PC방의 설치수 sum 구하기
			Long installCnt = gamePatchLogs.stream().filter(gamePatchLog -> {

				Long pcbIpTotal = pcbangIpTotalMap.get(gamePatchLog.getPcbId());
				if (pcbIpTotal == null) return false;

				if (gamePatchLog.getPatch()*10L > pcbIpTotal*3L) {
					return true;
				} else {
					return false;
				}
			}).count();

			//각 game별로 설치 IP수 sum 구하기
			Long installIpCnt = gamePatchLogs.stream().mapToLong(gamePatchLog -> gamePatchLog.getPatch()).sum();

			if (logger.isInfoEnabled()) {
				logger.info("game:" + game.getGsn() + ", install count:" + installCnt + ", install ip count:" + installIpCnt);
			}			
			
			GamePatchHistory newHistory = new GamePatchHistory(game.getGsn(), installCnt, installIpCnt, new Date());
			
			GamePatchHistory oldHistory = gamePatchHistoryDao.findByDateKeyAndGsn(newHistory.getDateKey(), newHistory.getGsn());
			
			if (oldHistory != null) {
				oldHistory.setInstall(installCnt);
				oldHistory.setInstallIpCnt(installIpCnt);
				gamePatchHistoryDao.save(oldHistory);
			} else {
				gamePatchHistoryDao.save(newHistory);
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("end history batch");
		}
	}

	public List<History> getHistorysBetween(String startKey, String endKey) {
		return historyDao.findByDateKeyBetweenOrderByDateKeyDesc(startKey, endKey);
	}

	public List<GamePatchHistory> getGamePatchHistorysBetween(String startKey, String endKey) {
		return gamePatchHistoryDao.findByDateKeyBetween(startKey, endKey);
	}
}
