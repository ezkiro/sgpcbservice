package com.toyfactory.pcb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.Pcbang;

import lombok.Data;

//PC방별 game patch 결과 ( view에 사용이 된다.)
@Data
public class PcbGamePatchResult {
	private Pcbang pcbang;
	private YN isPaymentPcbang;
	private Long totalIPCnt;
	private Long checkIPCnt;
	
	private Map<String, Long> gamePatchMap; //key:gsn val:patch count
	
	public PcbGamePatchResult(Pcbang pcbang) {
		this.pcbang = pcbang;
		this.gamePatchMap = new HashMap<String, Long>();
		this.isPaymentPcbang = YN.N;
		this.totalIPCnt = pcbang.getIpTotal();
		this.checkIPCnt = 0L;
	}

	public void buildResult(List<GamePatchLog> gamePatchLogs, List<Game> games){

		//init from games
		for (Game game : games) {
			gamePatchMap.put(game.getGsn(), 0L);
		}
		
		for (GamePatchLog gamePatchLog : gamePatchLogs) {
			gamePatchMap.put(gamePatchLog.getGsn(), gamePatchLog.getPatch());
			
			//checkIPCnt는  Install의 최대 값을 구해야 한다.
			if (checkIPCnt < gamePatchLog.getInstall()) checkIPCnt = gamePatchLog.getInstall();			
		}
	}	
}
