package com.toyfactory.pcb.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.toyfactory.pcb.model.StatusCd;

import lombok.Data;

@Entity
@Data
public class Pcbang {
	@Id
	@GeneratedValue
	private Long pcbId;
	private String companyCode; //관리업체2
	private String companyName;
	private String ceo;
	private String address;
	private String ipStart;
	private String ipEnd;
	private String submask;
	private String program;
	private Long ipTotal;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=10)
	private StatusCd status;
	
	@ManyToOne
	@JoinColumn(name = "agent_id", insertable = false, updatable = false)  //read only
	private Agent agent;
	
	@Temporal(TemporalType.TIMESTAMP)	
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;
	
	protected Pcbang(){}
	
	public void setAgent(Agent agent) {		
		if(this.agent != null) {
			this.agent.getPcbangs().remove(this);
		}
		
		this.agent = agent;
		agent.getPcbangs().add(this);
	}
	
	public Pcbang(Date date) {
		this.crtDt = date;
		this.uptDt = date;
	}
}
