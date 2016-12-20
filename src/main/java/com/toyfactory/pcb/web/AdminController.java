package com.toyfactory.pcb.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.service.MemberService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private MemberService memberService;	
	
	@RequestMapping("/agent")
	public String adminPage(Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		model.addAttribute("agentList",agentList);
		return "agentAdmin";
	}

	@RequestMapping("/pcbang")
	public String pcbangPage(Model model){
		
		model.addAttribute("pcbangList",null);
		return "pcbangAdmin";
	}
	
	@RequestMapping("/gamepatch")
	public String gamePatchPage(Model model){
		
		model.addAttribute("gamePatchList",null);
		return "gamePatchAdmin";
	}

	@RequestMapping("/pcbang/add")
	public String pcbangInputPage(Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		model.addAttribute("agentList",agentList);
		return "pcbangInput";
	}	
	
}
