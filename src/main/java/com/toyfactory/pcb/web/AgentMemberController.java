package com.toyfactory.pcb.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.toyfactory.pcb.model.YN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/member")
public class AgentMemberController {

	private static final Logger logger = LoggerFactory.getLogger(AgentMemberController.class);

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
	public String pcbangPage(
			@AgentArg Agent agent,
			@RequestParam(value="search_key", required = false) String searchKey,
			@RequestParam(value="search_value", required = false) String searchValue,
			Model model){

		if (logger.isDebugEnabled()) {
			logger.debug("searchKey:" + searchKey + ", searchValue:" + searchValue);
		}

		List<Pcbang> pcbangList;

		if (!StringUtils.isEmpty(searchKey) && !StringUtils.isEmpty(searchValue)) {
			pcbangList = memberService.findPcbangs(agent.getAgentId(), searchKey, searchValue);
			model.addAttribute("search_key",searchKey);
			model.addAttribute("search_value",searchValue);
		} else {
			//all pcbang
			pcbangList = agent.getPcbangs();
		}

		model.addAttribute("pcbangCnt", pcbangList.size());
		model.addAttribute("pcbangList",pcbangList);
		return "pcbangAgent";
	}
	
	@RequestMapping("/gamepatch")
	@PcbAuthorization(permission="AGENT")	
	public String gamePatchPage(
			@AgentArg Agent agent,
			@RequestParam(value="checked_games", required = false) String[] checkedGames,
			@RequestParam(value="search_key", required = false) String searchKey,
			@RequestParam(value="search_value", required = false) String searchValue,
			HttpServletRequest request,
			Model model){

		if (logger.isDebugEnabled()) {
			//logger.debug("checked_games:" + checkedGames);
			logger.debug("searchKey:" + searchKey + ", searchValue:" + searchValue);
		}

		//게임갯수가 많지 않기 때문에 전체를  가져와서 필터링 하는 방식으로 한다.
		List<Game> targetGames = gameService.buildGames(checkedGames);
		List<Game> allGames = gameService.findGames();

		if (checkedGames == null || checkedGames.length == 0) {
			targetGames = allGames;
		}

		List<Pcbang> pcbangs = null;

		if (!StringUtils.isEmpty(searchKey) && !StringUtils.isEmpty(searchValue)) {
			pcbangs = pcbangService.findPcbangs(agent.getAgentId(), searchKey, searchValue);
			model.addAttribute("search_key",searchKey);
			model.addAttribute("search_value",searchValue);
		} else {
			//status = OK 인 모든 pc방에 대해서 game patch 여부 조사
			pcbangs = pcbangService.findPcbangs(agent.getAgentId(), "all", null);
		}

		List<PcbGamePatchResult> pcbGamePatchResultList = gamePatchService.buildPcbGamePathResultForPcbangV2(pcbangs, targetGames);

		model.addAttribute("gameList",allGames);
		model.addAttribute("targetGameList",targetGames);

		long paymentPcbCnt = 0;

		if ("patchYN".equals(searchKey)) {
			List<PcbGamePatchResult> pcbGamePatchResultListByPatchYN = new ArrayList<PcbGamePatchResult>();

			for (PcbGamePatchResult item :pcbGamePatchResultList) {
				if (item.getIsPaymentPcbang() == YN.valueOf(searchValue)) {
					pcbGamePatchResultListByPatchYN.add(item);
				}
			}

			if ("Y".equals(searchValue)) {
				paymentPcbCnt = pcbGamePatchResultListByPatchYN.size();
			}

			model.addAttribute("pcbGamePatchResultList",pcbGamePatchResultListByPatchYN);
		} else {
			model.addAttribute("pcbGamePatchResultList", pcbGamePatchResultList);

			paymentPcbCnt = pcbGamePatchResultList.stream()
					.filter(pcbGamePatch -> pcbGamePatch.getIsPaymentPcbang() == YN.Y).count();
		}

		model.addAttribute("paymentPcbCnt", paymentPcbCnt);

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
