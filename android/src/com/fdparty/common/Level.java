package com.fdparty.common;

public enum Level {
	NORMAL("群众",0),
	INTERST("积极分子",1),
	INTERN("预备党员",2),
	STAFF("党员",3),
	LEADER("党支部副书记",4);
	
	
	
	
	private String context;
	private int status;
	
	private Level(String context, int status) {
		this.context = context;
		this.status = status;
	}
	
	public String toString(){
		return this.context;
	}
	
	public static String getNextLevel(int stat){
//		stat++;
		return getLevel(++stat);
	}

	public static String getLevel(int stat) {
		for(Level l : Level.values()){
			if(l.getStat() == stat)
				return l.toString();
		}
		return null;
	}

	public int getStat() {
		return this.status;
	}
}
