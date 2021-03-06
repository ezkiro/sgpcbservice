package com.toyfactory.pcb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.toyfactory.pcb.model.StatusCd;

import lombok.Data;

@Entity
@Data
public class Agent {
	@Id
	@GeneratedValue
	private Long agentId;
	private String companyCode;	//사업자번호
	private String companyName;	//상호
	private String ceo;			//대표자
	private String contactNum;	//연락처
	private String address;		//주소
	private String email;		//email
	private String bankAccount;	//계좌번호

	@OneToOne(mappedBy = "agent", optional = false, fetch=FetchType.LAZY)
	private Account account;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false, length=10)	
	private StatusCd status;		//상태 (승인/미승인/대기)
	
	@OneToMany
	@JoinColumn(name = "agent_id")
	private List<Pcbang> pcbangs = new ArrayList<Pcbang>();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;

	protected Agent(){}
	
	public Agent(Date date){
		this.crtDt = date;
		this.uptDt = date;
		this.status = StatusCd.WAIT;
	}	
}
