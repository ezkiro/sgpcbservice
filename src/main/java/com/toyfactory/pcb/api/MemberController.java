package com.toyfactory.pcb.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
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
    		@RequestParam(value="company_code", required = true) String companyCode,
    		@RequestParam(value="company_name", required = true) String companyName,
    		@RequestParam(value="ceo", required = false) String ceo,
    		@RequestParam(value="contact_num", required = true) String contactNum,
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="email", required = false) String email,
    		@RequestParam(value="bank_account", required = false) String bankAccount   		
    		) {
    	//check item : id, company code, contact, email
    	Map<String, String> params = new HashMap<String, String>();    	
    	
    	params.put("id", id);
    	params.put("password", password);
    	params.put("companyCode", companyCode);
    	params.put("companyName", companyName);
    	params.put("contactNum", contactNum);
    	
    	if(!StringUtils.isEmpty(ceo)) {
        	params.put("ceo", ceo);
    	}

    	if(!StringUtils.isEmpty(address)) {
        	params.put("address", address);
    	}

    	if(!StringUtils.isEmpty(email)) {
        	params.put("email", email);
    	}

    	if(!StringUtils.isEmpty(bankAccount)) {
        	params.put("bankAccount", bankAccount);
    	}
    	    	
    	return memberService.signUp(params);
    }
    
    @RequestMapping(value = "/pcbang/add", method=RequestMethod.POST)
    public boolean registerPcbang(
    		@RequestParam(value="company_code", required = true) String companyCode,
    		@RequestParam(value="company_name", required = true) String companyName,
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="start_ip", required = true) String startIp,
    		@RequestParam(value="end_ip", required = true) String endIp,
    		@RequestParam(value="submask", required = false) String submask,
    		@RequestParam(value="agent_id", required = true) Long agentId,
    		@RequestParam(value="program", required = false) String program
    		) {
    	
    	Pcbang aPcbang = new Pcbang(new Date());
    	aPcbang.setCompanyCode(companyCode);
    	aPcbang.setCompanyName(companyName);
    	aPcbang.setAddress(address);
    	aPcbang.setIpStart(startIp);
    	aPcbang.setIpEnd(endIp);
    	aPcbang.setSubmask(submask);
    	aPcbang.setProgram(program);
    	aPcbang.setStatus(StatusCd.WAIT);

    	return (null != memberService.addPcbang(aPcbang, agentId));
    }
    
    @RequestMapping(value = "/pcbang", method=RequestMethod.POST)
    public boolean updatePcbang(
    		@RequestParam(value="company_code", required = true) String companyCode,
    		@RequestParam(value="company_name", required = true) String companyName,
    		@RequestParam(value="address", required = false) String address,
    		@RequestParam(value="start_ip", required = true) String startIp,
    		@RequestParam(value="end_ip", required = true) String endIp,
    		@RequestParam(value="submask", required = false) String submask,
    		@RequestParam(value="agent_id", required = true) Long agentId,
    		@RequestParam(value="program", required = false) String program,
    		@RequestParam(value="pcb_id", required = false) Long pcbId,
    		@RequestParam(value="status", required = false) String status 		
    		) {
    	
    	Pcbang aPcbang = new Pcbang(new Date());
    	aPcbang.setCompanyCode(companyCode);
    	aPcbang.setCompanyName(companyName);
    	aPcbang.setAddress(address);
    	aPcbang.setIpStart(startIp);
    	aPcbang.setIpEnd(endIp);
    	aPcbang.setSubmask(submask);
    	aPcbang.setProgram(program);
    	aPcbang.setPcbId(pcbId);
    	
    	//agent member 가 수정하는 경우는 status는 변경을 할수 없기 때문에 기존 값을 유지해야 한다.
    	if(status != null)
    		aPcbang.setStatus(StatusCd.valueOf(status));
    	
    	return (null != memberService.updatePcbang(aPcbang, agentId));
    }    
    
    
}
