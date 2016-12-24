package com.toyfactory.pcb.api;


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

import com.toyfactory.pcb.model.PcbGamePatch;
import com.toyfactory.pcb.model.Version;

@RestController
@RequestMapping("/agent")
public class AgentController {

	private static final Logger log = LoggerFactory.getLogger(AgentController.class);	

    @Autowired @Qualifier("jsonRedisTemplate")
    private RedisTemplate<String, PcbGamePatch> redisTemplate;    	
	
    @RequestMapping("/version")
    public Version version(@RequestParam(value="name", defaultValue="World") String name) {
        return new Version("20161205");
    }
    
    @RequestMapping(value = "/gamepatch/{client_ip}", method = RequestMethod.PUT)
    public boolean putPcbGamePatch(@PathVariable String client_ip ,@RequestBody PcbGamePatch pcbGamePatch) throws Exception {
        redisTemplate.opsForValue().set(client_ip, pcbGamePatch);
        return true;
    }

    @RequestMapping(value = "/gamepatch/{client_ip}", method = RequestMethod.GET)
    public PcbGamePatch getPcbGamePatch(@PathVariable String client_ip) throws Exception {
        return redisTemplate.opsForValue().get(client_ip);
    }
}
