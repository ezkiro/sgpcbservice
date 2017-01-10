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
	
	private Long install = 0L; //설치된 수
	private Long patch = 0L; //패치 OK 수
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected GamePatchLog() {}

	public GamePatchLog(Long pcbId, String gsn) {
		this.pcbId = pcbId;
		this.gsn = gsn;
		this.install = 0L;
		this.patch = 0L;
		
		this.crtDt = new Date();
		this.uptDt = new Date();
	}
	
	public void incrPatch() {
		this.patch += 1L;
	}
	
	public void incrInstall() {
		this.install += 1L;
	}

}
