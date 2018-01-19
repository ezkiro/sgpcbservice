package com.toyfactory.pcb.model;

import java.util.List;

import com.toyfactory.pcb.domain.Game;
import com.toyfactory.pcb.domain.InstallPath;
import lombok.Data;

@Data
public class GameCommand {
	private String gsn;
	private String verifyType; //INSTALL/VERFILE
	private String exeFile;
	private String verFile;
	private String verFileFmt;
	private String verKey;
	
	private List<InstallPath> expectedPaths;
	
	//for cache
	public GameCommand() {}
	
	public GameCommand(Game game) {
		this.gsn = game.getGsn();
		this.verifyType = game.getVerifyType().toString();
		this.exeFile = game.getExeFile();
		this.verFile = game.getVerFile();
		this.verFileFmt = game.getVerFileFmt();
		this.verKey = "version";
	}
}
