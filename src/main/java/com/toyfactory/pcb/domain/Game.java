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
	private String name;
	@Column(nullable=false, length=50)
	private String major; //major version
	@Column(nullable=true, length=50)
	private String minor; //minor version
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date launchDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected Game(){}
}
