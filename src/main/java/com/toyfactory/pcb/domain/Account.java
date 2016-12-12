package com.toyfactory.pcb.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.toyfactory.pcb.model.Permission;

import lombok.Data;

@Entity
@Data
public class Account {
	@Id
	private String id;
	private String passwd;
	
	@Enumerated(EnumType.STRING)
	private Permission permission;
	
	@OneToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected Account(){};
	
	public Account(String id, String passwd, Permission permission) {
		this.id = id;
		this.passwd = passwd;
		this.permission = permission;
		this.crtDt = new Date();
		this.uptDt = new Date();
	}

/*
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
*/
	
}


