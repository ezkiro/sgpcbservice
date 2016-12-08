package com.toyfactory.pcb.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//import lombok.Data;

@Entity
//@Data
public class Account {

	@Id
	private String id;
	private String passwd;
	private String permission;
	private String agentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updDt;
	
	protected Account(){};
	
	public Account(String id, String passwd, String permission) {
		this.id = id;
		this.passwd = passwd;
		this.permission = permission;
		this.crtDt = new Date();
		this.updDt = new Date();
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getPasswd() {
		return this.passwd;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public String getAgentId() {
		return this.agentId;
	}
	
	public Date getCrtDt() {
		return this.crtDt;
	}
	
	public Date getUpdDt() {
		return this.updDt;
	}
}


