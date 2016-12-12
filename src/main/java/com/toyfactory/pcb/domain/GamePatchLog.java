package com.toyfactory.pcb.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class GamePatchLog {
	@Id
	@GeneratedValue	
	private Long id;
	private String ip; //clinet ip
	private String gsn;
	private String major; //major version
	private String minor; //minor version
	private String desc;
	private String exeFile;

	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected GamePatchLog(){}
	
}
