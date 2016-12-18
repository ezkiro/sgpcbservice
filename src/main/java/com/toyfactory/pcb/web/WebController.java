package com.toyfactory.pcb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.toyfactory.pcb.service.MemberService;

@Controller
public class WebController {

	@Autowired
	private MemberService memberService;	
	
	@RequestMapping("/main")
	public String jspPage(@RequestParam(value="name", required = true) String name, Model model){
		model.addAttribute("name","access token is" + name);
		return "hello";
	}	

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginPage(@RequestParam(value="error", required = false) String error, Model model){
		model.addAttribute("error",error);
		return "login";
	}

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login(
    		@RequestParam(value="id", required = true) String id,
    		@RequestParam(value="password", required = true) String password 
    		) {

    	String accessToken = memberService.authenticate(id, password);
    	
    	if(accessToken.isEmpty()) {
        	return "redirect:/login?error=invaild id or password";    		
    	}
    	
    	return "redirect:/main?name=" + accessToken;
    }
	
	@RequestMapping("/signup")
	public String signupPage(Model model){
		//model.addAttribute("name","hello springBoot1234");
		return "signup";
	}	
}
