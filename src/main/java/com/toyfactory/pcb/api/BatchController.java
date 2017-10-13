package com.toyfactory.pcb.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
    @PostMapping("/gamepatchlog")
    public String buildGamePatchLog(@RequestParam(value="auth_key", required = true) String authKey) {
    	
    	if (logger.isInfoEnabled()) {
    		logger.info("batch gamepatchlog: auth_key:" + authKey);
    	}
    	
    	if (!AUTH_KEY.equals(authKey)) {
    		
    		return "fail: invaild auth key!";
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
}
