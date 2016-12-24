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
}
