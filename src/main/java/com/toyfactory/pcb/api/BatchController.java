package com.toyfactory.pcb.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.toyfactory.pcb.config.PcbProperties;
import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.toyfactory.pcb.service.GamePatchService;

@RestController
@RequestMapping("/batch")
public class BatchController {
	private static final Logger logger = LoggerFactory.getLogger(BatchController.class);
	
	private static final String AUTH_KEY = "bb@tch99ey!";
	
	@Autowired
	private GamePatchService gamePatchService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private GameService gameService;

	@Autowired
	private PcbProperties pcbProperties;

	@GetMapping("/config")
	public String getConfig() {
		return "config:" + pcbProperties.toString();
	}

    @PostMapping("/gamepatchlog")
    public String buildGamePatchLog(@RequestParam(value="auth_key", required = true) String authKey) {
    	
    	if (logger.isInfoEnabled()) {
    		logger.info("batch gamepatchlog: auth_key:" + authKey);
    	}
    	
    	if (!AUTH_KEY.equals(authKey)) {
    		
    		return "fail: invalid auth key!";
    	}
    	
		//reference http://tutorials.jenkov.com/java-util-concurrent/executorservice.html
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(new Runnable() {
		    public void run() {
				gamePatchService.excuteGamePatchAnalysisBatch();
				historyService.executeHistoryBatch(gameService.findEnableGames());
		    }
		});
		
		executorService.shutdown();
				
		return "success";
    }


    @PostMapping("/reset/history/{key}")
    public String resetHistory(@RequestParam(value="auth_key", required = true) String authKey,
							   @PathVariable String key) {

		if (logger.isInfoEnabled()) {
			logger.info("batch resetHistory: auth_key:" + authKey);
		}

		if (!AUTH_KEY.equals(authKey)) {

			return "fail: invalid auth key!";
		}

		return historyService.resetHistory(key);
	}
}
