package com.toyfactory.pcb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchLog;
import com.toyfactory.pcb.domain.GamePatchLogPK;
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

	public void buildResult(List<GamePatchLog> gamePatchLogs, List<Game> games, long installCnt){

		//init from games
		for (Game game : games) {
			gamePatchMap.put(game.getGsn(), 0L);
		}
		
		//mapping gsn:gamePatchLog
		for (GamePatchLog gamePatchLog : gamePatchLogs) {
			gamePatchMap.put(gamePatchLog.getGsn(), gamePatchLog.getPatch());
			
			//checkIPCnt는  Install의 최대 값을 구해야 한다.
			if (checkIPCnt < gamePatchLog.getInstall()) checkIPCnt = gamePatchLog.getInstall();			
		}
		
		//check mission complete
		isPaymentPcbang = isMissionCompletePcbang(games, installCnt) ? YN.Y : YN.N;
		
	}
	
	public boolean isMissionCompletePcbang(List<Game> games, long installCnt) {
		//현재 지급대상 PC방은 모든 게임의 patch 수가  전체 IP 수의 50% 이상이어야 한다.
						
		for (Game aGame : games) {
			Long patchCnt = gamePatchMap.get(aGame.getGsn());

			//잘못된 PC방 IP 정보로 인해서 전체 IP수가 0이 나오면 무조건 false 반환
			if (pcbang.getIpTotal() == 0L) {
				return false;
			}

			// 10 ip 미만 설치이면 정산 PC방이 아니다.
			if (patchCnt < installCnt) {
				return false;
			}			
		}
		
		return true;
	}
	
}
