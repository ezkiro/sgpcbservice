package com.toyfactory.pcb.api;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.PcbGamePatch;
import com.toyfactory.pcb.model.Version;
import com.toyfactory.pcb.service.GamePatchService;
import com.toyfactory.pcb.service.PcbangService;

@RestController
@RequestMapping("/agent")
public class AgentController {

	private static final Logger logger = LoggerFactory.getLogger(AgentController.class);	
    
    @Autowired
    private GamePatchService gamePatchService;
    
    @Autowired
    private PcbangService pcbangService;
    
    @RequestMapping("/version")
    public Version version(@RequestParam(value="name", defaultValue="World") String name) {
        return new Version("20161205");
    }
    
    @RequestMapping(value = "/gamepatch/{client_ip}", method = RequestMethod.POST)
    public boolean putPcbGamePatch(@PathVariable String client_ip ,@RequestBody PcbGamePatch pcbGamePatch)throws Exception{
    	
    	if(logger.isDebugEnabled()){
        	logger.debug("[pcbgamepatch] ip:" + client_ip + ", data:" + pcbGamePatch.toString());    		
    	}
    	
        return gamePatchService.writePcbGamePatchToCache(client_ip, pcbGamePatch);
    }

    @RequestMapping(value = "/gamepatch/{client_ip}", method = RequestMethod.GET)
    public PcbGamePatch getPcbGamePatch(@PathVariable String client_ip) throws Exception {
        return gamePatchService.readPcbGamePatchFromCache(client_ip);
    }
    
    @RequestMapping(value = "/pcbang/{pcbid}", method = RequestMethod.GET)
    public List<String> getPcbangIPs(@PathVariable Long pcbid){
    	
    	Pcbang pcbang = pcbangService.findPcbang(pcbid);
    	if(pcbang == null ) return null;
    	
        return pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
    }
}
