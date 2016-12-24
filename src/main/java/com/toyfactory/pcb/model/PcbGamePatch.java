package com.toyfactory.pcb.model;

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
	private List<PcbGame> pcbGames;	
}