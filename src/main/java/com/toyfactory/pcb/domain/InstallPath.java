package com.toyfactory.pcb.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
public class InstallPath {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable=false, length=10)
	private String gsn;
	
	@Column(nullable=false, length=500)
	private String path;
	
	@Column(nullable=false, length=10)
	private String type; //ver|exe
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uptDt;	
}
