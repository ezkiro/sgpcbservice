package com.toyfactory.pcb.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
public @Data class Account implements Serializable {

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
}
