package com.toyfactory.pcb.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class Game {
	@Id
	private String gsn;
	private String name;
	private String major; //major version
	private String minor; //minor version
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date launchDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected Game(){}
}
