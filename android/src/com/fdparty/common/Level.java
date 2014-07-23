package com.fdparty.common;

public enum Level {
	NORMAL("Ⱥ��",0),
	INTERST("��������",1),
	INTERN("Ԥ����Ա",2),
	STAFF("��Ա",3),
	LEADER("��֧�������",4);
	
	
	
	
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
