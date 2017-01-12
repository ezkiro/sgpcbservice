package com.toyfactory.pcb.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.toyfactory.pcb.model.VerifyType;
import com.toyfactory.pcb.repository.PcbangRepository;
import com.toyfactory.pcb.service.GamePatchService;
import com.toyfactory.pcb.service.GameService;
import com.toyfactory.pcb.service.MemberService;
import com.toyfactory.pcb.service.PcbangService;

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
	
	@Autowired	
	private PcbangService pcbangService;	
	
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
		
		//status = OK 인 모든 pc방에 대해서 game patch 여부 조사
		List<Pcbang> pcbangs = pcbangService.findPcbangs("status", StatusCd.OK.toString());	
		List<Game> games = gameService.findGames();
				
		List<PcbGamePatchResult> pcbGamePatchResultList = gamePatchService.buildPcbGamePathResultForPcbang(pcbangs, games);
		
		model.addAttribute("gameList",games);
		model.addAttribute("pcbGamePatchResultList",pcbGamePatchResultList);
		
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
    		@RequestParam(value="verify_type", required = true) String verifyType,    		
    		@RequestParam(value="major", required = true) String majorVer,
    		@RequestParam(value="minor", required = false) String minorVer,
    		@RequestParam(value="exe_file", required = true) String exeFile,
    		@RequestParam(value="dir_name", required = false) String dirName,
    		@RequestParam(value="ver_file", required = false) String verFile,
    		@RequestParam(value="ver_file_fmt", required = false) String verFileFmt,
			@PathVariable String action){
				
		if (action.equals("new")) {
			Game newGame = new Game(new Date());
			
			newGame.setGsn(gsn);
			newGame.setName(name);
			newGame.setVerifyType(VerifyType.valueOf(verifyType));			
			newGame.setMajor(majorVer);
			newGame.setMinor(minorVer);
			newGame.setExeFile(exeFile);
			newGame.setDirName(dirName);
			newGame.setVerFile(verFile);
			newGame.setVerFileFmt(verFileFmt);
			
			return gameService.addGame(newGame);
			
		} else {
			
			Game existGame = gameService.findGame(gsn);
			
			if(existGame == null ) return false;			
			
			existGame.setName(name);
			existGame.setVerifyType(VerifyType.valueOf(verifyType));
			existGame.setMajor(majorVer);
			existGame.setExeFile(exeFile);
			
			if (!StringUtils.isEmpty(minorVer)) existGame.setMinor(minorVer);
			if (!StringUtils.isEmpty(dirName)) existGame.setDirName(dirName);
			if (!StringUtils.isEmpty(verFile)) existGame.setVerFile(verFile);
			if (!StringUtils.isEmpty(verFileFmt)) existGame.setVerFileFmt(verFileFmt);
			
			return gameService.updateGame(existGame);
		}		
	}
	
	@RequestMapping("/batch")
	@ResponseBody
	@PcbAuthorization(permission="ADMIN")	
	public String runBatch(){

		gamePatchService.excuteGamePatchAnalysisBatch();
		
		return "OK";
	}	
	
}
