package com.toyfactory.pcb.model;

import lombok.Data;

/**
 * Pcbang agent 가 수집하는 개별 게임 정보
 * @author hikiro
 *
 */
@Data
public class PcbGame {
	private String gsn;
	private String name;
	private String major;
	private String minor;
	
//	@Override
//	public String toString(){
//		
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("gsn:").append(gsn)
//		  .append(",name:").append(name)
//		  .append(",major:").append(major)
//		  .append(",minor:").append(minor);
//		
//		return sb.toString();
//	}
}
