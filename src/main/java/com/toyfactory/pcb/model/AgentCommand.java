package com.toyfactory.pcb.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class AgentCommand {
	private String cmd;
	private List<GameCommand> gameCommands;
	
	//for cache
	public AgentCommand() {}
	
	public AgentCommand(String command) {
		this.cmd = command;
		this.gameCommands = new ArrayList<GameCommand>();
	}
}
