package com.toyfactory.pcb.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toyfactory.pcb.aop.PcbAuthorization;
import com.toyfactory.pcb.domain.Agent;
import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.Pcbang;
import com.toyfactory.pcb.model.PcbGamePatch;
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

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);	
	
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

	@RequestMapping("/gamepatch/excel")
	@PcbAuthorization(permission="ADMIN")	
	public String gamePatchExcelDownload(Model model){
		
		return "gamePatchExcelDownload";
	}	
	
	@RequestMapping("/gamepatch")
	@PcbAuthorization(permission="ADMIN")	
	public String gamePatchPage(
			@RequestParam(value="checked_games", required = false) String[] checkedGames,
			@RequestParam(value="search_key", required = false) String searchKey,
			@RequestParam(value="search_value", required = false) String searchValue,
			Model model){

		if (logger.isDebugEnabled()) {
			//logger.debug("checked_games:" + checkedGames);
			logger.debug("searchKey:" + searchKey + ", searchValue:" + searchValue);
		}
		
		//게임갯수가 많지 않기 때문에 전체를  가져와서 필터링 하는 방식으로 한다.
		List<Game> targetGames = gameService.buildGames(checkedGames);
		List<Game> allGames = gameService.findGames();
		
		if (checkedGames == null) {
			targetGames = allGames;
		}
		
		List<Pcbang> pcbangs = null;
		
		if (!StringUtils.isEmpty(searchKey) && !StringUtils.isEmpty(searchValue)) {
			pcbangs = pcbangService.findPcbangs(searchKey, searchValue);
			model.addAttribute("search_key",searchKey);
			model.addAttribute("search_value",searchValue);			
		} else {
			//status = OK 인 모든 pc방에 대해서 game patch 여부 조사
			pcbangs = pcbangService.findPcbangs("status", StatusCd.OK.toString());				
		}
				
		List<PcbGamePatchResult> pcbGamePatchResultList = gamePatchService.buildPcbGamePathResultForPcbang(pcbangs, targetGames);
		
		model.addAttribute("gameList",allGames);
		model.addAttribute("targetGameList",targetGames);		
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

	@RequestMapping(value="/api/game/remove", method=RequestMethod.POST)
	@ResponseBody
	@PcbAuthorization(permission="ADMIN")	
	public boolean removeGame(
			@RequestParam(value="gsn_list[]", required = true) String[] gsnList) {
	
		for (String gsn : gsnList) {
			gameService.removeGame(gsn);
		}
		
		return true;
	}	
	
	@RequestMapping("/batch")
	@ResponseBody
	@PcbAuthorization(permission="ADMIN")	
	public String runBatch(){
		
		//reference http://tutorials.jenkov.com/java-util-concurrent/executorservice.html
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(new Runnable() {
		    public void run() {
				gamePatchService.excuteGamePatchAnalysisBatch();
		    }
		});
		
		executorService.shutdown();
				
		return "OK";
	}
	
	@RequestMapping("/pcbgamepatch/detail")
	@PcbAuthorization(permission="ADMIN")	
	public String showPcbGamePatch(Model model,
			@RequestParam(value="pcb_id", required = true) Long pcbId){

		Pcbang pcbang = pcbangService.findPcbang(pcbId);
		List<String> pcbangIPs = pcbangService.buildPcbangIPs(pcbang.getIpStart(), pcbang.getIpEnd(), pcbang.getSubmask());
		
		List<PcbGamePatch> pcbGamePatchList = new ArrayList<PcbGamePatch>();
		
		for (String ip : pcbangIPs) {
			PcbGamePatch pcbGamePatch = gamePatchService.readPcbGamePatchFromCache(ip);
			if (pcbGamePatch == null) continue;			
			pcbGamePatch.setClientIp(ip);
			pcbGamePatchList.add(pcbGamePatch);
		}			
		
		model.addAttribute("pcbGamePatchList",pcbGamePatchList);
		model.addAttribute("pcbang",pcbang);
		model.addAttribute("totalIPs",pcbangIPs.size());
		
		return "pcbGamePatchLog";
	}	
	

	@GetMapping("/pcbang/upload")
	@PcbAuthorization(permission="ADMIN")		
	public String uploadBulkPcbangPage(Model model) {
		
        return "pcbangBulkUpload";				
	}	
	
	@PostMapping("/pcbang/upload")
	@PcbAuthorization(permission="ADMIN")		
	public String uploadBulkPcbangList(
			@RequestParam(value = "file", required = true) MultipartFile file,
            RedirectAttributes redirectAttributes) {
		
		if(logger.isDebugEnabled()) {
			logger.debug("[uploadBulkPcbangList] filename:" + file.getOriginalFilename());
		}
		
		pcbangService.insertBulkData(file);
	
		redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/admin/pcbang";				
	}
}
