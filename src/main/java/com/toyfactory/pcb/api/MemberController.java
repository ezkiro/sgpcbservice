package com.toyfactory.pcb.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.toyfactory.pcb.exception.AuthenticationException;
import com.toyfactory.pcb.resolver.AgentArg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.aop.PcbAuthorization;
import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.service.MemberService;
import com.toyfactory.pcb.service.PcbangService;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);	
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PcbangService pcbangService;	
	
    @RequestMapping("/isExist")
    public boolean duplicateCheck(
    		@RequestParam(value="item", required = true) String item,
    		@RequestParam(value="value", required = true) String value 
    		) {
    	//check item : id, company code, contact, email
    	return memberService.checkDuplicateItem(item, value);
    }
    
    @RequestMapping(value = "/signUp", method=RequestMethod.POST)
    public boolean signUp(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="password", required = true) String password,
    		@RequestParam(value="company_code", required = false) String companyCode,
    		@RequestParam(value="company_name", required = false) String companyName,
    		@RequestParam(value="ceo", required = false) String ceo,
    		@RequestParam(value="contact_num", required = false) String contactNum,
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="email", required = false) String email,
    		@RequestParam(value="bank_account", required = false) String bankAccount   		
    		) {
    	//check item : id, company code, contact, email
    	Map<String, String> params = new HashMap<String, String>();    	
    	
    	params.put("id", id);
    	params.put("password", password);
    	
    	if (!StringUtils.isEmpty(companyCode)) {
        	params.put("companyCode", companyCode);
    	}
    	if (!StringUtils.isEmpty(companyName)) {
        	params.put("companyName", companyName);
    	}
    	if (!StringUtils.isEmpty(contactNum)) {
        	params.put("contactNum", contactNum);
    	}
    	
    	if (!StringUtils.isEmpty(ceo)) {
        	params.put("ceo", ceo);
    	}

    	if (!StringUtils.isEmpty(address)) {
        	params.put("address", address);
    	}

    	if (!StringUtils.isEmpty(email)) {
        	params.put("email", email);
    	}

    	if (!StringUtils.isEmpty(bankAccount)) {
        	params.put("bankAccount", bankAccount);
    	}
    	    	
    	return memberService.signUp(params);
    }
  
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="password", required = true) String password, 
    		HttpServletResponse response) {

		try {
			String accessToken = memberService.authenticate(id, password);

			Cookie cookie;
			cookie = new Cookie("access_token", URLEncoder.encode(accessToken, "UTF-8"));
			cookie.setPath("/");
			cookie.setMaxAge(3600); //1시간 유효
			//if(!Strings.isNullOrEmpty(cookieDomain)) cookie.setDomain(cookieDomain);
			//if(!Strings.isNullOrEmpty(cookiePath)) cookie.setPath(cookiePath);
			
			response.addCookie(cookie);
			//go to process for permission

			if(accessToken.contains(Permission.ADMIN.toString())){
				return "/admin/agent";
			} else if (accessToken.contains(Permission.PARTNER.toString())) {
				return "/admin/gamepatch";
			} else {
				return "/member/gamepatch";
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (AuthenticationException e) {
			return "/login?error=" + e.getMessage();
		}
    }
    
    @RequestMapping(value = "/pcbang/add", method=RequestMethod.POST)
    @PcbAuthorization(permission="AGENT")
    public boolean registerPcbang(
    		@RequestParam(value="company_code", required = false) String companyCode,
    		@RequestParam(value="company_name", required = true) String companyName,
    		@RequestParam(value="ceo", required = true) String ceo,    		
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="start_ip", required = true) String startIp,
    		@RequestParam(value="end_ip", required = true) String endIp,
    		@RequestParam(value="submask", required = true) String submask,
    		@RequestParam(value="agent_id", required = true) Long agentId,
    		@RequestParam(value="program", required = false) String program
    		) {
    	
    	Pcbang aPcbang = new Pcbang(new Date());
    	aPcbang.setCompanyCode(companyCode);
    	aPcbang.setCompanyName(companyName);
    	aPcbang.setCeo(ceo);
    	aPcbang.setAddress(address);
    	aPcbang.setIpStart(startIp);
    	aPcbang.setIpEnd(endIp);
    	aPcbang.setSubmask(submask);
    	aPcbang.setProgram(program);
    	aPcbang.setStatus(StatusCd.WAIT);

    	return (null != memberService.addPcbang(aPcbang, agentId));
    }
    
    @RequestMapping(value = "/pcbang", method=RequestMethod.POST)
    @PcbAuthorization(permission="AGENT")    
    public boolean updatePcbang(
    		@RequestParam(value="pcb_id", required = true) Long pcbId,    		
    		@RequestParam(value="company_code", required = false) String companyCode,
    		@RequestParam(value="company_name", required = true) String companyName,
    		@RequestParam(value="ceo", required = true) String ceo,    		
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="start_ip", required = true) String startIp,
    		@RequestParam(value="end_ip", required = true) String endIp,
    		@RequestParam(value="submask", required = true) String submask,
    		@RequestParam(value="agent_id", required = true) Long agentId,
    		@RequestParam(value="program", required = false) String program,
    		@RequestParam(value="status", required = false) String status 		
    		) {
    	
    	Pcbang aPcbang = pcbangService.findPcbang(pcbId);
    	aPcbang.setCompanyName(companyName);
    	aPcbang.setCeo(ceo);
    	aPcbang.setIpStart(startIp);
    	aPcbang.setIpEnd(endIp);
    	aPcbang.setSubmask(submask);

    	if (!StringUtils.isEmpty(companyCode))    	
    		aPcbang.setCompanyCode(companyCode);    	
    	
    	if (!StringUtils.isEmpty(address))
    		aPcbang.setAddress(address);
    	
    	if (!StringUtils.isEmpty(program))
    		aPcbang.setProgram(program);
    	
    	//agent member 가 수정하는 경우는 status는 변경을 할수 없기 때문에 기존 값을 유지해야 한다.
    	if (!StringUtils.isEmpty(status))
    		aPcbang.setStatus(StatusCd.valueOf(status));
    	
    	return (null != memberService.updatePcbang(aPcbang, agentId));
    }    
  
	@RequestMapping(value="/pcbang/remove", method=RequestMethod.POST)
	@ResponseBody
	@PcbAuthorization(permission="ADMIN")	
	public boolean removePcbang(
			@RequestParam(value="pcbid_list[]", required = true) String[] pcbIdList) {
	
		for (String pcbId : pcbIdList) {
			memberService.removePcbang(Long.valueOf(pcbId));
		}
		
		return true;
	}	
    
    
    @RequestMapping(value = "/agent", method=RequestMethod.POST)
    @PcbAuthorization(permission="ADMIN")   
    public boolean updateAgent(
    		@RequestParam(value="agent_id", required = true) Long agentId,
    		@RequestParam(value="status", required = true) String status,
    		@RequestParam(value="permission", required = true) String permission,
			@RequestParam(value="allow_ip", required = false) String allowIp,
    		@RequestParam(value="company_code", required = false) String companyCode,
    		@RequestParam(value="company_name", required = false) String companyName,
    		@RequestParam(value="ceo", required = false) String ceo,    		
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="contact_num", required = false) String contactNum,
    		@RequestParam(value="bank_account", required = false) String bankAccount,
    		@RequestParam(value="email", required = false) String email,
    		@RequestParam(value="password", required = false) String password    		
    		) {
    	
    	Agent aAgent = memberService.findAgent(agentId);
    	
    	if (aAgent == null) return false;
    	
    	aAgent.setStatus(StatusCd.valueOf(status));
    	if (!StringUtils.isEmpty(companyCode))
    		aAgent.setCompanyCode(companyCode);
    	
    	if (!StringUtils.isEmpty(companyName))
    		aAgent.setCompanyName(companyName);
    	
    	if (!StringUtils.isEmpty(ceo))
    		aAgent.setCeo(ceo);  	
    	
    	if (!StringUtils.isEmpty(address))
    		aAgent.setAddress(address);
    	
    	if (!StringUtils.isEmpty(contactNum))
    		aAgent.setContactNum(contactNum);
    	
    	if (!StringUtils.isEmpty(bankAccount))
    		aAgent.setBankAccount(bankAccount);
    	
    	if (!StringUtils.isEmpty(email))
    		aAgent.setEmail(email);
    	
    	//TODO: 단독으로 변경하는 I/F 추가 필요
    	if (!StringUtils.isEmpty(password))
    		memberService.changePassword(aAgent, password);

		if (!StringUtils.isEmpty(allowIp))
			memberService.changeAllowIp(aAgent, allowIp.replaceAll(" ", ""));
    	    	
    	return (null != memberService.updateAgent(aAgent, Permission.valueOf(permission)));
    }    

    @RequestMapping(value = "/agent/unregister", method=RequestMethod.POST)
    @PcbAuthorization(permission="ADMIN")   
    public boolean unregisterAgent(
    		@RequestParam(value="agent_id", required = true) Long agentId) {

    	Agent aAgent = memberService.findAgent(agentId);
    	
    	if (aAgent == null) return false;

    	memberService.unregisterAgent(aAgent);
    	
    	return true;
    }

	@RequestMapping(value = "/myagent", method=RequestMethod.POST)
	@PcbAuthorization(permission="AGENT")
	public boolean updateMyAgent(
			@AgentArg Agent aAgent,
			@RequestParam(value="allow_ip", required = false) String allowIp,
			@RequestParam(value="company_code", required = false) String companyCode,
			@RequestParam(value="company_name", required = false) String companyName,
			@RequestParam(value="ceo", required = false) String ceo,
			@RequestParam(value="address", required = false) String address,
			@RequestParam(value="contact_num", required = false) String contactNum,
			@RequestParam(value="bank_account", required = false) String bankAccount,
			@RequestParam(value="email", required = false) String email,
			@RequestParam(value="password", required = false) String password
	) {
		if (aAgent == null) return false;

		if (!StringUtils.isEmpty(companyCode))
			aAgent.setCompanyCode(companyCode);

		if (!StringUtils.isEmpty(companyName))
			aAgent.setCompanyName(companyName);

		if (!StringUtils.isEmpty(ceo))
			aAgent.setCeo(ceo);

		if (!StringUtils.isEmpty(address))
			aAgent.setAddress(address);

		if (!StringUtils.isEmpty(contactNum))
			aAgent.setContactNum(contactNum);

		if (!StringUtils.isEmpty(bankAccount))
			aAgent.setBankAccount(bankAccount);

		if (!StringUtils.isEmpty(email))
			aAgent.setEmail(email);
		//TODO: 단독으로 변경하는 I/F 추가 필요
		if (!StringUtils.isEmpty(password))
			memberService.changePassword(aAgent, password);

		if (!StringUtils.isEmpty(allowIp))
			memberService.changeAllowIp(aAgent, allowIp.replaceAll(" ", ""));

		return (null != memberService.updateMyAgent(aAgent));
	}
    
}
