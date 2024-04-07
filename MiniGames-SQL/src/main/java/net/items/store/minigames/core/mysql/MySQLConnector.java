package net.items.store.minigames.core.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnector {
	
	private String host;
	private String database;
	private String user;
	private String password;

	private Connection connection;

	public MySQLConnector(String host, String database, String user, String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
	}

	public boolean connect() {
		try {
			checkInfo(this.host, "Host");
			checkInfo(this.database, "Database");
			checkInfo(this.user, "User");
			checkInfo(this.password, "Password");
			
			String url = "jdbc:mysql://" + this.host + ":3306/" + this.database + "?autoReconnect=true&characterEncoding=utf8";
			Properties properties = new Properties();
			properties.setProperty("user", this.user);
			properties.setProperty("password", this.password);
			
			connection = DriverManager.getConnection(url, properties);
			
			System.out.println("Die Verbindung zur MySQL Datenbank wurde erfolgreich aufgebaut!");
			return true;
		} catch (SQLException e) {
			System.out.println("Die Verbindung zur MySQL Datenbank konnte nicht aufgebaut werden!");
			System.out.println("Fehler: " + e.getMessage());
			e.printStackTrace();
			connection = null;
		} catch (MySQLException e) {
			sendCannotConnectOut(e.getMessage());
			connection = null;
		}
		return false;
	}
	
	public boolean isConnected() {
		if(connection != null) {
			return true;
		}
		return false;
	}
	
	private void checkInfo(String info, String infoName) throws MySQLException {
		if(info == null || info.equalsIgnoreCase("null")) {
			throw new MySQLException(infoName);
		}
	}
	
	private void sendCannotConnectOut(String reason) {
		System.out.println("Die Verbindung zur MySQL Datenbank konnte nicht aufgebaut werden!");
		System.out.println("Grund: " + reason);
	}

	public boolean close() {
		try {
			if (connection != null) {
				connection.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Connection getConnection() {
		return connection;
	}

}
