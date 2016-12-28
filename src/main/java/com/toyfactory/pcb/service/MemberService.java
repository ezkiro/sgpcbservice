package com.toyfactory.pcb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.toyfactory.pcb.domain.Account;
import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.repository.AccountRepository;
import com.toyfactory.pcb.repository.AgentRepository;
import com.toyfactory.pcb.repository.PcbangRepository;

@Service("memberService")
public class MemberService {

	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);	
	
	@Autowired
	private AccountRepository accountDao;	

	@Autowired
	private AgentRepository agentDao;
	
	@Autowired	
	private PcbangRepository pcbangDao;
	
	@Autowired	
	private CrpytoService crpytoService;
	
	
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
		
		if(StringUtils.isEmpty(params.get("id"))|| StringUtils.isEmpty(params.get("password"))) {
			logger.error("[signUp] fail to sign up! id or password is null or empty!");
			return false;
		}
		
		String passwordHash = crpytoService.generateSHA1Hash(params.get("password"));
		
		Account newMember = new Account(params.get("id"),passwordHash, Permission.AGENT);
					
		Agent newAgent = new Agent(new Date());

		//set optional information		
		if(!StringUtils.isEmpty(params.get("companyCode"))) {
			newAgent.setCompanyCode(params.get("companyCode"));			
		}	
		
		if(!StringUtils.isEmpty(params.get("companyName"))) {
			newAgent.setCompanyName(params.get("companyName"));			
		}

		if(!StringUtils.isEmpty(params.get("contactNum"))) {
			newAgent.setContactNum(params.get("contactNum"));			
		}		
		
		if(!StringUtils.isEmpty(params.get("address"))) {
			newAgent.setAddress(params.get("address"));			
		}

		if(!StringUtils.isEmpty(params.get("ceo"))) {
			newAgent.setCeo(params.get("ceo"));
		}

		if(!StringUtils.isEmpty(params.get("bankAccount"))) {
			newAgent.setBankAccount(params.get("bankAccount"));
		}
		if(!StringUtils.isEmpty(params.get("email"))) {
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
		
		String passwordHash = crpytoService.generateSHA1Hash(password);		
		
		Account user = accountDao.findByIdAndPassword(id, passwordHash);
		
		if(user == null) {
			logger.error("authentication fail! wrong password id:" + id);
			return accessToken;
		}
		
		StringBuilder tokenBuilder = new StringBuilder();
		
		Agent agent = user.getAgent();
		tokenBuilder.append(String.valueOf(agent.getAgentId()))
					.append("|")
					.append(user.getPermission())
					.append("|")
					.append(new Date().getTime() + 3600);
					
		String sha1hash = crpytoService.generateSHA1Hash(tokenBuilder.toString());
					
		tokenBuilder.append("|")
					.append(sha1hash);
		accessToken =  tokenBuilder.toString();
		
		return accessToken;
	}
	
	public List<Agent> findAgents(String item, String keyworkd) {
		//all agents
		return agentDao.findAll();		
	}

	public List<Pcbang> findPcbangs(String item, String keyworkd) {
		//all pcbangs
		return pcbangDao.findAll();		
	}
		
	public Pcbang addPcbang(Pcbang pcbang, Long agentId) {		
		Agent agent = agentDao.findOne(agentId);		
		//맵핑할 Agent 가 없으면 입력 실패
		if(agent == null ) return null;
		
		pcbang.setAgent(agent);
		
		return pcbangDao.save(pcbang);
	}
	
	public Pcbang updatePcbang(Pcbang pcbang, Long agentId) {		
		Agent agent = agentDao.findOne(agentId);		
		//맵핑할 Agent 가 없으면 입력 실패
		if(agent == null ) return null;
		
		pcbang.setAgent(agent);
		pcbang.setUptDt(new Date());
		
		return pcbangDao.save(pcbang);
	}
	
	
}
