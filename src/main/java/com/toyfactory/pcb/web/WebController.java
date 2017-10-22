package com.toyfactory.pcb.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.toyfactory.pcb.aop.PcbAuthorization;
import com.toyfactory.pcb.model.Permission;
import com.toyfactory.pcb.service.MemberService;

@Controller
public class WebController {

//	@Autowired
//	private MemberService memberService;	
	
	@RequestMapping("/hello")
//	@PcbAuthorization(permission="NOBODY")
	public String helloPage(@RequestParam(value="name", required = false) String name, Model model){
		//model.addAttribute("name","access token is" + name);
		return "hello";
	}	

	@RequestMapping("/errormsg")
	public String errorPage(@RequestParam(value="message", required = false) String message, Model model){
		if(message != null){
			model.addAttribute("message", message);			
		}
		return "errorMsg";
	}		
	
	@RequestMapping(value={"/", "/login"}, method=RequestMethod.GET)
	public String loginPage(@RequestParam(value="error", required = false) String error, Model model){
		model.addAttribute("error",error);
		return "login";
	}

    @RequestMapping(value="/logout")
    public String logout(HttpServletResponse response) {
    	
		Cookie cookie = new Cookie("access_token", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		
	    return "redirect:/v2/login";
    }
    
    
	@RequestMapping("/signup")
	public String signupPage(Model model){
		//model.addAttribute("name","hello springBoot1234");
		return "signup";
	}	
}
