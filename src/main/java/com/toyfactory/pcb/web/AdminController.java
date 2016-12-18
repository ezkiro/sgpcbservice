package com.toyfactory.pcb.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping("/agent")
	public String adminPage(Model model){
		return "agentAdmin";
	}
	
}
