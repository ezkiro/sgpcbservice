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
import com.toyfactory.pcb.exception.InvalidTokenException;
import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.model.StatusCd;
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
	private PcbangService pcbangService;	
	
	@Autowired	
	private CrpytoService crpytoService;
	
	
	public boolean checkDuplicateItem(String item, String value) {
    	//check item : id, company code, contact, email
    	
		logger.debug("input item=" + item + ", value=" + value);
		
    	if ("id".equals(item)) {
    		Account account = accountDao.findOne(value);
    		if( account != null) return true;
    		else return false;
    	}

    	if ("company_code".equals(item)) {
    		Agent agent = agentDao.findByCompanyCode(value);
    		if ( agent != null) return true;
    		else return false;   		
    	}
    	
    	if ("contact".equals(item)) {
    		Agent agent = agentDao.findByContactNum(value);
    		if( agent != null) return true;
    		else return false;    		
    	}
    	
    	if ("email".equals(item)) {
    		Agent agent = agentDao.findByEmail(value);
    		if( agent != null) return true;
    		else return false;     		
    	}
    	
        return true;		
	}
	
	
	public boolean signUp(Map<String,String> params){

		logger.debug("input params=" + params);
		
		if (StringUtils.isEmpty(params.get("id"))|| StringUtils.isEmpty(params.get("password"))) {
			logger.error("[signUp] fail to sign up! id or password is null or empty!");
			return false;
		}
		
		String passwordHash = crpytoService.generateSHA1Hash(params.get("password"));
		
		Account newMember = new Account(params.get("id"),passwordHash, Permission.AGENT);
					
		Agent newAgent = new Agent(new Date());

		//set optional information		
		if (!StringUtils.isEmpty(params.get("companyCode"))) {
			newAgent.setCompanyCode(params.get("companyCode"));			
		}	
		
		if (!StringUtils.isEmpty(params.get("companyName"))) {
			newAgent.setCompanyName(params.get("companyName"));			
		}

		if (!StringUtils.isEmpty(params.get("contactNum"))) {
			newAgent.setContactNum(params.get("contactNum"));			
		}		
		
		if (!StringUtils.isEmpty(params.get("address"))) {
			newAgent.setAddress(params.get("address"));			
		}

		if (!StringUtils.isEmpty(params.get("ceo"))) {
			newAgent.setCeo(params.get("ceo"));
		}

		if (!StringUtils.isEmpty(params.get("bankAccount"))) {
			newAgent.setBankAccount(params.get("bankAccount"));
		}
		if (!StringUtils.isEmpty(params.get("email"))) {
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
		
		if (user == null) {
			logger.error("authentication fail! wrong password id:" + id);
			return accessToken;
		}
		
		StringBuilder tokenBuilder = new StringBuilder();
		
		Permission permission = Permission.NOBODY;
		//admin,partner 와 agent간 권한 분리
		if (user.getPermission() == Permission.ADMIN || user.getPermission() == Permission.PARTNER) {
			//0 이라는 특수한 agent id를 사용한다.
			tokenBuilder.append(String.valueOf(0));
			permission = user.getPermission();
			
		} else {
			Agent agent = user.getAgent();
			
			if (agent.getStatus() == StatusCd.OK) {
				permission = user.getPermission();
			}		
			
			tokenBuilder.append(String.valueOf(agent.getAgentId()));			
		}
				
		tokenBuilder.append("|")
					.append(permission.toString())
					.append("|")
					.append(new Date().getTime() + 3600 * 1000);
					
		String sha1hash = crpytoService.generateSHA1Hash(tokenBuilder.toString());
					
		tokenBuilder.append("|")
					.append(sha1hash);
		accessToken =  tokenBuilder.toString();
		
		return accessToken;
	}
	
	public boolean changePassword(Agent agent, String password) {		
		Account account = agent.getAccount();
		if (account != null) {
			account.setPassword(crpytoService.generateSHA1Hash(password));
			account.setUptDt(new Date());
			
			accountDao.save(account);
			return true;
		}
		
		return false;
	}
	
	
	public List<Agent> findAgents(String item, String keyworkd) {
		
		if ("status".equals(item)) {
			return agentDao.findByStatus(StatusCd.valueOf(keyworkd));
		}	
		
		//all agents
		return agentDao.findAllByOrderByCrtDtDesc();		
	}
	
	public Agent findAgent(Long agentId){
		return agentDao.findOne(agentId);
	}
	

	public List<Pcbang> findPcbangs(String key, String keyword) {

		if ("agentName".equals(key)) {
			return pcbangDao.findByAgentName(keyword);
		}
		
		if ("companyCode".equals(key)) {
			return pcbangDao.findByCompanyCode(keyword);
		}
		
		if ("companyName".equals(key)) {
			return pcbangDao.findByCompanyNameContaining(keyword);
		}

		if ("ipRange".equals(key)) {
			// 100.1.1.1 -> 100.1.1% search
			String ipKey = keyword.substring(0,keyword.lastIndexOf("."));
			if (logger.isDebugEnabled()) logger.debug("[findPcbangs] ipKey:" + ipKey);
			return pcbangDao.findByIpStartStartingWith(ipKey);
		}
		
		if ("program".equals(key)) {
			return pcbangDao.findByProgram(keyword);
		}
		
		if ("all".equals(key)) {
			return pcbangDao.findAll();
		}		
		
		//key:status
		if ("status".equals(key)) {
			return pcbangDao.findByStatus(StatusCd.valueOf(keyword));
		}
		
		//all pcbangs order by uptdt desc
		return pcbangDao.findAllByOrderByUptDtDesc();		
	}
		
	public Pcbang addPcbang(Pcbang pcbang, Long agentId) {		
		Agent agent = agentDao.findOne(agentId);		
		//맵핑할 Agent 가 없으면 입력 실패
		if (agent == null ) return null;
		
		//ipStart 와 ipEnd가 동일하면 중복 PC방으로 보고 입력하지 않는다.
		List<Pcbang> duplicatePcbangs = pcbangDao.findByIpStartAndIpEnd(pcbang.getIpStart(),pcbang.getIpEnd());
		
		if (!duplicatePcbangs.isEmpty()) {	
			logger.error("[addPcbang] duplicate pcbang! ipStart:" + pcbang.getIpStart() + ", ipEnd:" + pcbang.getIpEnd());			
			return null;
		}

		//아래 같이 set을 하면 반듯이 save를 해 줘야 한다.
		pcbang.setAgent(agent);

		List<String> pcbangIPs = pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
		pcbang.setIpTotal(Long.valueOf(pcbangIPs.size()));
				
		return pcbangDao.save(pcbang);
	}
	
	public Pcbang updatePcbang(Pcbang pcbang, Long agentId) {		
		Agent agent = agentDao.findOne(agentId);		
		//맵핑할 Agent 가 없으면 입력 실패
		if (agent == null ) return null;
		
		pcbang.setAgent(agent);
		pcbang.setUptDt(new Date());
		
		List<String> pcbangIPs = pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
		pcbang.setIpTotal(Long.valueOf(pcbangIPs.size()));
		
		return pcbangDao.save(pcbang);
	}
	
	public void removePcbang(Long pcbId) {
		
		pcbangDao.delete(pcbId);
	}
	
	public Permission verifyAccessToken(String accessToken) throws InvalidTokenException{
		
		if (accessToken == null) throw new InvalidTokenException();
		
		if (logger.isDebugEnabled()) logger.debug("verifyAccessToken accessToken:" + accessToken);
		
		//access tokekn format :  [agent_id|permission|expire date|sha1 hash]
		
		String delimiter = "\\|";
		
		String[] tokens = accessToken.split(delimiter);	
		
		if (tokens.length < 4) throw new InvalidTokenException();
				
		StringBuilder message = new StringBuilder();
		
		message.append(tokens[0]).append("|").append(tokens[1]).append("|").append(tokens[2]);
				
		String messageDigest = tokens[3];
				
		if (logger.isDebugEnabled()) logger.debug("verifyAccessToken message:" + message.toString() + ", hash:" + messageDigest);

		String checkHash = crpytoService.generateSHA1Hash(message.toString());
		
		if (logger.isDebugEnabled()) logger.debug("verifyAccessToken checkHash:" + checkHash);
		
		//compare messageDigest
		if (!messageDigest.equals(checkHash)) {
			if(logger.isDebugEnabled()) logger.debug("verifyAccessToken fail to compare messageDigest");			
				throw new InvalidTokenException();
		}
		
		//check expire time
		Date now = new Date();
		Date tokenExpire = new Date(Long.valueOf(tokens[2]));
		
		if (now.getTime() > tokenExpire.getTime()) {
			if(logger.isDebugEnabled()) logger.debug("verifyAccessToken token time is expired");			
				throw new InvalidTokenException();
		}
		
		return Permission.valueOf(tokens[1]);
	}
	
	public Agent updateAgent(Agent agent, Permission permission) {
		
		Account account = agent.getAccount();
		if (account != null && account.getPermission() != permission) {
			account.setPermission(permission);
			account.setUptDt(new Date());
			
			accountDao.save(account);
		}
		
		agent.setUptDt(new Date());
						
		return agentDao.save(agent);
	}
	
	public void unregisterAgent(Agent agent) {
		//delete Pcbang
//		List<Pcbang> pcbangs = agent.getPcbangs();
//		
//		for (Pcbang pcbang : pcbangs) {
//			if(logger.isDebugEnabled()) logger.debug("unregisterAgent try to delete pcbang:" + pcbang.getPcbId());			
//			pcbangDao.delete(pcbang);
//		}

		//delete account
		Account account = agent.getAccount();		
		accountDao.delete(account);
		
		//delete agent
		agentDao.delete(agent.getAgentId());
	}
}
