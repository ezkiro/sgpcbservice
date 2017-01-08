package com.toyfactory.pcb.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class Game {
	@Id
	@Column(nullable=false, length=10)
	private String gsn;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false, length=50)
	private String major; //major version
	@Column(nullable=true, length=50)
	private String minor; //minor version

	@Column(nullable=false)	
	private String exeFile;
	
	private String dirName;

	private String verFile;
	
	private String verFileFmt; //JSON/XML/BIN
	
	private String etc;	
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected Game(){}
	
	public Game(Date date) {
		this.crtDt = date;
		this.uptDt = date;
	}
}
