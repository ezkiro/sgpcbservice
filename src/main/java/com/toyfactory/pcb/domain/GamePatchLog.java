package com.toyfactory.pcb.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
@IdClass(value=GamePatchLogPK.class)
public class GamePatchLog {
	@Id
	private Long pcbId;
	@Id
	@Column(nullable=false, length=10)
	private String gsn;
	
	@Column(nullable=false, length=50)
	private String major; //major version
	@Column(nullable=true, length=50)	
	private String minor; //minor version
	private String desc;

	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected GamePatchLog(){}

	public GamePatchLog(Long pcbId, String gsn, String major, String minor) {
		this.pcbId = pcbId;
		this.gsn = gsn;
		this.major = major;
		this.minor = minor;
		
		this.crtDt = new Date();
		this.uptDt = new Date();
	}
}
