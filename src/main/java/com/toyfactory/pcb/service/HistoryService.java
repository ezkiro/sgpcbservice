package com.toyfactory.pcb.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

import javax.transaction.Transactional;

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
		

		List<Pcbang> paidPcbangs = pcbangs.stream().filter(pcbang -> gamePatchService.isMissionCompletePcbang(pcbang, games) == true).collect(Collectors.toList());

		//정산  PC방수
		Long paidPcbCnt = Long.valueOf(paidPcbangs.size());

		//정산 PC방의 총 IP 수
		Long paidIpCnt = paidPcbangs.stream().mapToLong(pcbang-> pcbang.getIpTotal()).sum();

		if (logger.isInfoEnabled()) {
			logger.info("all pcb count:" + pcbCnt + ", paid pcbCnt:" + paidPcbCnt + ", paidIpCnt:" + paidIpCnt);
		}
		
		//history 저장
		historyDao.save(new History(pcbCnt, paidPcbCnt, paidIpCnt, new Date()));
		
		for(Game game : games) {
			List<GamePatchLog> gamePatchLogs = gamePatchLogDao.findByGsn(game.getGsn());

			//각 game별로  PC방의 설치수 sum 구하기
			Long installCnt = gamePatchLogs.stream().filter(gamePatchLog -> gamePatchLog.getPatch() > 0L).count();

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

	@Transactional
	public String resetHistory(String key) {

		if ("all".equals(key)) {
			historyDao.deleteAll();
			gamePatchHistoryDao.deleteAll();
			logger.info("[resetHistory] deleteAll");
		} else {
			try {
				//key는 yyyyMMdd 형식
				SimpleDateFormat input = new SimpleDateFormat("yyyyMMdd");
				Date crtDt = input.parse(key);
				historyDao.deleteByCrtDtBefore(crtDt);
				gamePatchHistoryDao.deleteByCrtDtBefore(crtDt);
				logger.info("[resetHistory] deleteBefore crtDt:{}", crtDt);

			} catch (ParseException e) {
				logger.error("[resetHistory] ParseException error:{}", e.getMessage());
				return "fail! invalid key:" + key;
			}
		}

		return "success";
	}
}
