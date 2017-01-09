package com.toyfactory.pcb.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private MemberService memberService;
		
	@Autowired
	private GamePatchService gamePatchService;
	
	@Autowired	
	private GameService gameService;
	
	@Autowired	
	private PcbangRepository pcbangDao;	
	
	@RequestMapping("/agent")
	@PcbAuthorization(permission="ADMIN")	
	public String agentPage(Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		int agentOKCnt = 0;
		int agentWaitCnt = 0;
		
		for(Agent agent : agentList) {
			if(agent.getStatus() == StatusCd.OK) agentOKCnt += 1;
			if(agent.getStatus() == StatusCd.WAIT) agentWaitCnt += 1;
		}
		
		model.addAttribute("agentList",agentList);
		model.addAttribute("agentOKCnt",agentOKCnt);
		model.addAttribute("agentWaitCnt",agentWaitCnt);
		
		return "agentAdmin";
	}

	@RequestMapping("/agent/update")
	public String agentUpdatePage(
			@RequestParam(value="agent_id", required = true) Long agentId,
			Model model){
		
		Agent agent = memberService.findAgent(agentId);
			
		model.addAttribute("agent",agent);			
				
		return "agentUpdate";
	}	
	
	
	@RequestMapping("/pcbang")
	@PcbAuthorization(permission="ADMIN")	
	public String pcbangPage(Model model){
		
		List<Pcbang> pcbangList = memberService.findPcbangs(null, null);

		model.addAttribute("pcbangCnt", pcbangList.size());
		model.addAttribute("pcbangList",pcbangList);
		return "pcbangAdmin";
	}
	
	@RequestMapping("/gamepatch")
	@PcbAuthorization(permission="ADMIN")	
	public String gamePatchPage(Model model){
		
		List<Pcbang> pcbangs = memberService.findPcbangs(null, null);	
		Map<Long, PcbGamePatchResult> gamePatchMapForPcbang = gamePatchService.buildGamePathForAllPcbang();
		
		List<Game> games = gameService.findGames();
	
		model.addAttribute("gameList",games);
		model.addAttribute("pcbangList",pcbangs);
		model.addAttribute("gamePatchMapForPcbang",gamePatchMapForPcbang);
		
		return "gamePatchAdmin";
	}

	@RequestMapping("/pcbang/add")
	@PcbAuthorization(permission="ADMIN")	
	public String pcbangAddPage(Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		model.addAttribute("agentList",agentList);
		return "pcbangInput";
	}	

	@RequestMapping("/pcbang/update")
	@PcbAuthorization(permission="ADMIN")	
	public String pcbangUpdatePage(
			@RequestParam(value="pcb_id", required = true) Long pcbId,
			Model model){
		
		List<Agent> agentList = memberService.findAgents(null, null);
		
		Pcbang pcbang = pcbangDao.findOne(pcbId);
		
		if(pcbang != null) {
			model.addAttribute("pcbang",pcbang);			
		}
				
		model.addAttribute("agentList",agentList);
		return "pcbangInput";
	}	

	@RequestMapping("/game")
	@PcbAuthorization(permission="ADMIN")	
	public String gamePage(Model model){
		
		List<Game> gameList = gameService.findGames();

		model.addAttribute("gameList",gameList);
		return "gameAdmin";
	}	


	@RequestMapping(value="/game/add", method=RequestMethod.GET)
	@PcbAuthorization(permission="ADMIN")	
	public String addGamePage(Model model){				
		return "gameInput";
	}
	
	@RequestMapping(value="/game/update", method=RequestMethod.GET)
	@PcbAuthorization(permission="ADMIN")	
	public String updateGamePage(Model model,
    		@RequestParam(value="gsn", required = true) String gsn){
		
		Game game = gameService.findGame(gsn);
		model.addAttribute("game",game);
		return  "gameInput";
	}	
	
	@RequestMapping(value="/game/{action}", method=RequestMethod.POST)
	@ResponseBody
	@PcbAuthorization(permission="ADMIN")	
	public boolean addOrUpdateGame(Model model,
    		@RequestParam(value="gsn", required = true) String gsn,
    		@RequestParam(value="name", required = true) String name,
    		@RequestParam(value="major", required = true) String majorVer,
    		@RequestParam(value="minor", required = false) String minorVer,
			@PathVariable String action){
		
		if(action.equals("new")){
			return gameService.addGame(gsn, name, majorVer, minorVer);
			
		} else {
			return gameService.updateGame(gsn, name, majorVer, minorVer);
		}		
	}	
}
