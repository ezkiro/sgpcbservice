package com.toyfactory.pcb.api;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.AgentCommand;
import com.toyfactory.pcb.model.PcbGamePatch;
import com.toyfactory.pcb.model.Version;
import com.toyfactory.pcb.service.GamePatchService;
import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.PcbangService;

@RestController
@RequestMapping("/agent")
public class AgentController {

	private static final Logger logger = LoggerFactory.getLogger(AgentController.class);	
    
    @Autowired
    private GamePatchService gamePatchService;
    
    @Autowired
    private PcbangService pcbangService;
    
    @Autowired
    private GameService gameService;
    
    @RequestMapping("/version")
    public Version version(@RequestParam(value="name", defaultValue="World") String name) {
        return new Version("20161205");
    }
    
    @RequestMapping(value = "/gamepatch", method = RequestMethod.POST)
    public boolean putPcbGamePatch(@RequestParam(name="client_ip") String client_ip ,@RequestBody PcbGamePatch pcbGamePatch)throws Exception{
    	
    	if(logger.isInfoEnabled()){
        	logger.info("[pcbgamepatch] ip:" + client_ip + ", data:" + pcbGamePatch.toString());
    	}
    	
    	if("127.0.0.1".equals(client_ip)){
    		//skip 처리
    		logger.error("[pcbgamepatch] client ip is 127.0.0.1! data:" + pcbGamePatch.toString());
    		return true;
    	}    	
    	
        return gamePatchService.writePcbGamePatchToCache(client_ip, pcbGamePatch);
    }

    @RequestMapping(value = "/gamepatch", method = RequestMethod.GET)
    public PcbGamePatch getPcbGamePatch(@RequestParam(name="client_ip") String client_ip) throws Exception {
    	if(logger.isInfoEnabled()){
        	logger.info("[getPcbGamePatch] ip:" + client_ip);
    	}    	
    	
        return gamePatchService.readPcbGamePatchFromCache(client_ip);
    }
    
    @RequestMapping(value = "/pcbang/{pcbid}", method = RequestMethod.GET)
    public List<String> getPcbangIPs(@PathVariable Long pcbid){
    	
    	Pcbang pcbang = pcbangService.findPcbang(pcbid);
    	if(pcbang == null ) return null;
    	
        return pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
    }
    
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String preCheck(@RequestParam(name="client_ip") String client_ip) {

    	if(logger.isInfoEnabled()){
        	logger.info("[precheck] ip:" + client_ip);
    	}    	
    	
        return gamePatchService.checkGamePatchPass(client_ip);
    }
    
    @RequestMapping(value = "/command", method = RequestMethod.GET)
    public AgentCommand requestCommand(@RequestParam(name="client_ip") String client_ip) {
    	
    	if(logger.isInfoEnabled()){
        	logger.info("[requestCommand] ip:" + client_ip);
    	}    	
    	
    	return gameService.buildAgentCommand("CHECK");
    }
}
