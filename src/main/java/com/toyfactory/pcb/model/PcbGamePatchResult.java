package com.toyfactory.pcb.model;

import java.util.HashMap;
import java.util.Map;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.GamePatchLog;

import lombok.Data;

//PC방별 game patch 결과 ( view에 사용이 된다.)
@Data
public class PcbGamePatchResult {
	private Map<String, String> gamePatchMap; //key:gsn val:patch YN
	private YN allPatchYN;
	
	public PcbGamePatchResult() {
		gamePatchMap = new HashMap<String, String>();
		allPatchYN = YN.N;
	}

	public boolean verifyGamePatch(GamePatchLog gamePatchLog, Game game){

		if(!game.getMajor().equals(gamePatchLog.getMajor())) {
			gamePatchMap.put(game.getGsn(), "N");			
			return false;
		}

		gamePatchMap.put(game.getGsn(), "Y");
		return true;
	}
	
	public boolean verifyAllGamePatch(int totalGameCnt) {
		
		allPatchYN = YN.N;
		
		if(gamePatchMap.isEmpty()) return false;
		
		if(gamePatchMap.size() < totalGameCnt) return false;
		
		for(String gsn : gamePatchMap.keySet()) {
			if(!gamePatchMap.get(gsn).equals("Y")){
				return false;
			}
		}
		
		//모든 game의  patch 정보가 들어 있다는 가정 
		allPatchYN = YN.Y;
		return true;
	}	
}
