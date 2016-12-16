package com.toyfactory.pcb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.domain.Account;
import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.repository.AccountRepository;
import com.toyfactory.pcb.repository.AgentRepository;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private AccountRepository accountDao;	

	@Autowired
	private AgentRepository agentDao;	
	
    @RequestMapping("/isExist")
    public boolean duplicateCheck(
    		@RequestParam(value="item") String item,
    		@RequestParam(value="value") String value 
    		) {
    	//check item : id, company code, contact, email
    	
    	if("id".equals(item)) {
    		Account account = accountDao.findOne(value);
    		if( account != null) return true;
    		else return false;
    	}

    	if("company_code".equals(item)) {
    		Agent agent = agentDao.findByCompanyCode(value);
    		if( agent != null) return true;
    		else return false;   		
    	}
    	
    	if("contact".equals(item)) {
    		Agent agent = agentDao.findByContactNum(value);
    		if( agent != null) return true;
    		else return false;    		
    	}
    	
    	if("email".equals(item)) {
    		Agent agent = agentDao.findByEmail(value);
    		if( agent != null) return true;
    		else return false;     		
    	}
    	
        return false;
    }
}
