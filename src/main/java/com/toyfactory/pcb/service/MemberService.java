package com.toyfactory.pcb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toyfactory.pcb.domain.Account;
import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.repository.AccountRepository;
import com.toyfactory.pcb.repository.AgentRepository;

@Service("memberService")
public class MemberService {

	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);	
	
	@Autowired
	private AccountRepository accountDao;	

	@Autowired
	private AgentRepository agentDao;
	
	public boolean checkDuplicateItem(String item, String value) {
    	//check item : id, company code, contact, email
    	
		logger.debug("input item=" + item + ", value=" + value);
		
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
    	
        return true;		
	}
	
	
	public boolean signUp(Map<String,String> params){

		logger.debug("input params=" + params);
		
		if(Strings.isNullOrEmpty(params.get("id"))|| Strings.isNullOrEmpty(params.get("password"))) {
			logger.error("[signUp] fail to sign up! id or password is null or empty!");
			return false;
		}
		
		Account newMember = new Account(params.get("id"),params.get("password"), Permission.AGENT);
					
		Agent newAgent = new Agent(new Date());

		//set optional information		
		if(!Strings.isNullOrEmpty(params.get("companyCode"))) {
			newAgent.setCompanyCode(params.get("companyCode"));			
		}	
		
		if(!Strings.isNullOrEmpty(params.get("companyName"))) {
			newAgent.setCompanyName(params.get("companyName"));			
		}

		if(!Strings.isNullOrEmpty(params.get("contactNum"))) {
			newAgent.setContactNum(params.get("contactNum"));			
		}		
		
		if(!Strings.isNullOrEmpty(params.get("address"))) {
			newAgent.setAddress(params.get("address"));			
		}

		if(!Strings.isNullOrEmpty(params.get("ceo"))) {
			newAgent.setCeo(params.get("ceo"));
		}

		if(!Strings.isNullOrEmpty(params.get("bankAccount"))) {
			newAgent.setBankAccount(params.get("bankAccount"));
		}
		if(!Strings.isNullOrEmpty(params.get("email"))) {
			newAgent.setEmail(params.get("email"));
		}
		
		agentDao.save(newAgent);

		//link agent to account
		newMember.setAgent(newAgent);
		
		accountDao.save(newMember);
		
		return true;
	}
	
	public String authenticate(String id, String password) {
		//access tokekn format :  [agent_id|permission|expire date|sha1 hash]
		String accessToken = "";
		
		Account user = accountDao.findByIdAndPassword(id, password);
		
		if(user != null){
			StringBuilder tokenBuilder = new StringBuilder();
			
			Agent agent = user.getAgent();
			tokenBuilder.append(String.valueOf(agent.getAgentId()))
						.append("|")
						.append(user.getPermission())
						.append("|")
						.append(new Date().getTime() + 3600)
						.append("|")
						.append("sha1hash");
			accessToken =  tokenBuilder.toString();
		}
		
		return accessToken;
	}
	
	public List<Agent> findAgents(String item, String keyworkd) {
		//all agents
		return agentDao.findAll();		
	}
}
