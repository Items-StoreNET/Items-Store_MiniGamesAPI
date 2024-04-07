package net.items.store.minigames.core.mysql;

public class MySQLException extends Exception {

	private String info;
	private static final long serialVersionUID = 1L;

	public MySQLException(String info) {
		super("Die Info " + info + " wurde nicht richtig in der Config eingetragen.");

		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}

}
