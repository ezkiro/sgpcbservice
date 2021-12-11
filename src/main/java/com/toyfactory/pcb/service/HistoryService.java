package com.toyfactory.pcb.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.toyfactory.pcb.config.PcbProperties;
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

	@Autowired
	private PcbProperties pcbProperties;

	public void executeHistoryBatch(List<Game> games) {
		//반드시 excuteGamePatchAnalysisBatch() 이후에 실행이 되어야 한다.
		
		if (logger.isInfoEnabled()) {
			logger.info("start history batch");
		}
		
		List<Pcbang> pcbangs = pcbangDao.findByStatus(StatusCd.OK);
		//new History object

		//등록 PC방수
		Long pcbCnt = Long.valueOf(pcbangs.size());

		List<Pcbang> paidPcbangs = pcbangs.stream().filter(pcbang -> gamePatchService.isMissionCompletePcbang(pcbang, games, pcbProperties.getInstallCnt()) == true).collect(Collectors.toList());

		//지급  PC방수
		Long paidPcbCnt = Long.valueOf(paidPcbangs.size());

		//지급 PC방의 patch count의 sum
		Long paidPcbAllPatchCnt = gamePatchService.sumGamePatchCntForAllPaidPcbang(paidPcbangs, games.get(0));
		
		if (logger.isInfoEnabled()) {
			logger.info("all pcb count:" + pcbCnt + ", paid pcbCnt:" + paidPcbCnt);
		}
		
		//history 저장
		historyDao.save(new History(pcbCnt, paidPcbCnt, paidPcbAllPatchCnt, new Date()));
		
		for(Game game : games) {
			//각 game벼로  PC방의 설치수 sum 구하기
			Long installCnt = gamePatchLogDao.findByGsn(game.getGsn()).stream().filter(gamePatchLog -> gamePatchLog.getInstall() > 0L).count();

			if (logger.isInfoEnabled()) {
				logger.info("game:" + game.getGsn() + ", install count:" + installCnt);
			}			
			
			GamePatchHistory newHistory = new GamePatchHistory(game.getGsn(), installCnt, new Date());
			
			GamePatchHistory oldHistory = gamePatchHistoryDao.findByDateKeyAndGsn(newHistory.getDateKey(), newHistory.getGsn());
			
			if (oldHistory != null) {
				oldHistory.setInstall(installCnt);
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

	public History getHistory(Date date) {
		DateFormat keyFormat = new SimpleDateFormat("yyyyMMdd");
		return historyDao.findOne(keyFormat.format(date));
	}
}
