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
	//private YN isPaymentPcbang;
	private Long totalIPCnt;
	private Long checkIPCnt;
	
	private Map<String, Long> gamePatchMap; //key:gsn val:patch count
	private Map<String, String> gameVersionMap; //key:gsn val: game version
	private Map<String, YN> gamePaymentMap; //key:gsn val: isPayment YN

	public PcbGamePatchResult(Pcbang pcbang) {
		this.pcbang = pcbang;
		this.gamePatchMap = new HashMap<>();
		this.gameVersionMap = new HashMap<>();
		this.gamePaymentMap = new HashMap<>();
		//this.isPaymentPcbang = YN.N;
		this.totalIPCnt = pcbang.getIpTotal();
		this.checkIPCnt = 0L;
	}

	public void buildResult(List<GamePatchLog> gamePatchLogs, List<Game> games){

		//init from games
		for (Game game : games) {
			gamePatchMap.put(game.getGsn(), 0L);
		}
		
		//mapping gsn:gamePatchLog
		for (GamePatchLog gamePatchLog : gamePatchLogs) {
			gamePatchMap.put(gamePatchLog.getGsn(), gamePatchLog.getPatch());
			//게임별 최종 version 정보를 셋팅한다.
			gameVersionMap.put(gamePatchLog.getGsn(), gamePatchLog.getMajor());

			//checkIPCnt는  Install의 최대 값을 구해야 한다.
			if (checkIPCnt < gamePatchLog.getInstall()) checkIPCnt = gamePatchLog.getInstall();			
		}
		
		//check mission complete
		for (Game aGame : games) {
			Long patchCnt = gamePatchMap.get(aGame.getGsn());
			gamePaymentMap.put(aGame.getGsn(), YN.N);

			// 10개 이상 설치를 하면 성공으로 간주한다.
			if (patchCnt >= 10L) {
				gamePaymentMap.put(aGame.getGsn(), YN.Y);
			}
		}

		//isPaymentPcbang = isMissionCompletePcbang(games) ? YN.Y : YN.N;
		
	}

	public YN isMissionCompleteGame(Game game) {
		return this.gamePaymentMap.get(game.getGsn());
	}

	public boolean isMissionCompletePcbang(List<Game> games) {
		//현재 지급대상 PC방은 모든 게임의 patch 수가  전체 IP 수의 50% 이상이어야 한다.
						
		for (Game aGame : games) {
			Long patchCnt = gamePatchMap.get(aGame.getGsn());

			//잘못된 PC방 IP 정보로 인해서 전체 IP수가 0이 나오면 무조건 false 반환
			if (pcbang.getIpTotal() == 0L) {
				return false;
			}

			// 30% 미만은 지급 대상이 아니다. 아래와 같이 하는 것은 부동 소수점 연산을 피하기 위해서 이다.
			if ((patchCnt * 10L) < (pcbang.getIpTotal() *3L)) {
				return false;
			}
		}
		
		return true;
	}
	
}
