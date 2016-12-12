package com.toyfactory.pcb.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping("/")
	public String jspPage(Model model){
		model.addAttribute("name","hello springBoot1234");
		return "hello";
	}	
	
}
