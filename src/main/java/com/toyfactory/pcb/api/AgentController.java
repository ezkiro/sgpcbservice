package com.toyfactory.pcb.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.domain.Account;
import com.toyfactory.pcb.model.Version;
import com.toyfactory.pcb.repository.AccountRepository;

@RestController
@RequestMapping("/agent")
public class AgentController {

	private static final Logger log = LoggerFactory.getLogger(AgentController.class);	
	
	@Autowired
	private AccountRepository accountDao;	
	
    @RequestMapping("/version")
    public Version version(@RequestParam(value="name", defaultValue="World") String name) {
        return new Version("20161205");
    }
    
	@RequestMapping("/add/{id}/{passwd}")
	public Account add(@PathVariable String id,
			@PathVariable String passwd){

		Account account = new Account(id, passwd, "Admin");
		Account accountData = accountDao.save(account);
		log.debug("new account:" + accountData);
		return account;
	}
	
	@RequestMapping("/view/{id}")
	public Account view(@PathVariable String id){
		Account accountData = accountDao.findOne(id);
		return accountData;
	}	

	@RequestMapping("/list")
	public List<Account> list(){
		List<Account> accountList = accountDao.findAll();		
		return accountList;
	}
	
	@RequestMapping("/del/{id}")
	public String delete(@PathVariable String id){
		System.out.println("id=" + id);
		accountDao.delete(id);
		return "redirect:/agent/list";
	}
}
