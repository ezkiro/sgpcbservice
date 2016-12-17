package com.toyfactory.pcb.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class GamePatchLogPK implements Serializable {

	private Long pcbId;
	private String gsn;
	
	public GamePatchLogPK(){}
	
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GamePatchLogPK){
        	GamePatchLogPK gamePatchLogPk = (GamePatchLogPK) obj;
 
            if(gamePatchLogPk.getPcbId() != pcbId){
                return false;
            }
 
            if(!gamePatchLogPk.getGsn().equals(gsn)){
                return false;
            }
            
            return true;
        }
 
        return false;
    }
 
    @Override
    public int hashCode() {
        return pcbId.hashCode() + gsn.hashCode();
    }		
}
