package com.toyfactory.pcb.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Pcbang Agent가 1대의 PC에서 수집해서 보내느 정보
 * json으로 하면  {"pcbGames":[{"gsn":"10", "name":"game1", "major":"1234", "minor":""},{"gsn":"20", "name":"game2", "major":"1111", "minor":""}]}
 * @author hikiro
 *
 */
@Data
public class PcbGamePatch {
	private String version; //pcbAgent version
	private Date crtDt;
	private List<PcbGame> pcbGames;
	private String clientIp;
	
//	@Override
//	public String toString(){
//		StringBuilder sb = new StringBuilder("pcbGames:[");
//		
//		for(PcbGame pcbGame : pcbGames) {
//			sb.append("{").append(pcbGame.toString()).append("},");
//		}		
//		
//		sb.append("]");
//		return sb.toString();
//	}
}