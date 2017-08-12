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
 * 일자별로 다음 데이터를 저장하는 history
 * 일자 / 등록PC방 수 / 정산PC방 수 
 * @author ezkiro
 *
 */

@Entity
@Data
public class History {
	@Id
	@Column(nullable=false, length=8)	
	private String dateKey; //YYYMMdd 형태
	
	private Long pcbCnt;
	private Long paidPcbCnt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crtDt;
	
	protected History() {}
	
	public History(Long pcbCnt, Long paidPcbCnt, Date date) {
		
		DateFormat keyFormat = new SimpleDateFormat("yyyyMMdd");
		
		this.dateKey = keyFormat.format(date);
		this.pcbCnt = pcbCnt;
		this.paidPcbCnt = paidPcbCnt;
		this.crtDt = date;
	}
}
