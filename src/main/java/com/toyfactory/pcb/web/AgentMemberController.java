package com.toyfactory.pcb.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.toyfactory.pcb.aop.PcbAuthorization;
import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.PcbGamePatchResult;
import com.toyfactory.pcb.model.StatusCd;
import com.toyfactory.pcb.repository.PcbangRepository;
import com.toyfactory.pcb.resolver.AgentArg;
import com.toyfactory.pcb.service.GamePatchService;
import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.MemberService;
import com.toyfactory.pcb.service.PcbangService;

@Controller
@RequestMapping("/member")
public class AgentMemberController {
	@Autowired
	private MemberService memberService;
		
	@Autowired
	private GamePatchService gamePatchService;
	
	@Autowired	
	private GameService gameService;
	
	@Autowired	
	private PcbangService pcbangService;
	
	@RequestMapping("/pcbang")
	@PcbAuthorization(permission="AGENT")	
	public String pcbangPage(@AgentArg Agent agent, Model model){
		
		List<Pcbang> pcbangList = agent.getPcbangs();

		model.addAttribute("pcbangCnt", pcbangList.size());
		model.addAttribute("pcbangList",pcbangList);
		return "pcbangAgent";
	}
	
	@RequestMapping("/gamepatch")
	@PcbAuthorization(permission="AGENT")	
	public String gamePatchPage(@AgentArg Agent agent, Model model){
		
		List<Pcbang> pcbangs = pcbangService.findPcbangs(agent.getAgentId(), StatusCd.OK);
		List<Game> games = gameService.findGames();

		List<PcbGamePatchResult> pcbGamePatchResultList = gamePatchService.buildPcbGamePathResultForPcbang(pcbangs, games);
		
		model.addAttribute("gameList",games);
		model.addAttribute("pcbGamePatchResultList",pcbGamePatchResultList);
		
		return "gamePatchAgent";
	}

	@RequestMapping("/pcbang/add")
	@PcbAuthorization(permission="AGENT")	
	public String pcbangAddPage(@AgentArg Agent agent, Model model){
				
		model.addAttribute("agent",agent);
		return "pcbangInputAgent";
	}	

	@RequestMapping("/pcbang/update")
	@PcbAuthorization(permission="AGENT")	
	public String pcbangUpdatePage(
			@AgentArg Agent agent,
			@RequestParam(value="pcb_id", required = true) Long pcbId,
			Model model){
				
		Pcbang pcbang = pcbangService.findPcbang(pcbId);
		
		if(pcbang != null) {
			model.addAttribute("pcbang",pcbang);			
		}
				
		model.addAttribute("agent",agent);
		return "pcbangInputAgent";
	}	

}
