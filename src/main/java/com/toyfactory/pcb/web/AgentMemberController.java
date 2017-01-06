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
	public String pcbangPage(Model model){
		
		List<Pcbang> pcbangList = memberService.findPcbangs(null, null);

		model.addAttribute("pcbangCnt", pcbangList.size());
		model.addAttribute("pcbangList",pcbangList);
		return "pcbangAgent";
	}
	
	@RequestMapping("/gamepatch")
	@PcbAuthorization(permission="AGENT")	
	public String gamePatchPage(Model model){
		
		List<Pcbang> pcbangs = pcbangService.findPcbangs(1L, StatusCd.OK);
		Map<Long, PcbGamePatchResult> gamePatchMapForPcbang = gamePatchService.buildGamePathForAllPcbang();
		
		List<Game> games = gameService.findGames();
	
		model.addAttribute("gameList",games);
		model.addAttribute("pcbangList",pcbangs);
		model.addAttribute("gamePatchMapForPcbang",gamePatchMapForPcbang);
		
		return "gamePatchAgent";
	}

	@RequestMapping("/pcbang/add")
	@PcbAuthorization(permission="AGENT")	
	public String pcbangAddPage(Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		model.addAttribute("agentList",agentList);
		return "pcbangInput";
	}	

	@RequestMapping("/pcbang/update")
	@PcbAuthorization(permission="AGENT")	
	public String pcbangUpdatePage(
			@RequestParam(value="pcb_id", required = true) Long pcbId,
			Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		Pcbang pcbang = pcbangService.findPcbang(pcbId);
		
		if(pcbang != null) {
			model.addAttribute("pcbang",pcbang);			
		}
				
		model.addAttribute("agentList",agentList);
		return "pcbangInput";
	}	

}
