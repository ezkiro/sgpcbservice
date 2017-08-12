package com.toyfactory.pcb.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * 일자별로 다음데이터를 보관한다.
 * 일자/ gsn/ install
 * dateKey + gsn 으로 unique 하다
 * @author ezkiro
 *
 */

@Entity
@Data
public class GamePatchHistory {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false, length=8)	
	private String dateKey; //YYYMMdd 형태
	@Column(nullable=false, length=10)
	private String gsn;
	
	private Long install = 0L; //설치된 수
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	
	protected GamePatchHistory() {}
	
	public GamePatchHistory(String gsn, Long install, Date date) {
		DateFormat keyFormat = new SimpleDateFormat("yyyyMMdd");
		
		this.dateKey = keyFormat.format(date);		
		this.gsn = gsn;
		this.install = install;
		this.crtDt = date;
	}
}