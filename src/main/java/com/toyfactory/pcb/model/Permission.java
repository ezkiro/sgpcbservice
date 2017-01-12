package com.toyfactory.pcb.model;

public enum Permission {
	NOBODY(0),
	AGENT(1),
	PARTNER(2),
	ADMIN(3);
	
	private int level;
	public int getLevel() {return this.level;}
	public void setLevel(int level) {this.level = level;}
	private Permission(int level) {this.level = level;}
}
