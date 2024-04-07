package net.items.store.minigames.core.mysql;

import net.items.store.minigames.api.sql.IMySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL implements IMySQL {

	private MySQLConnector mySQLConnector;

	public MySQL(MySQLConnector mySQLConnector){
		this.mySQLConnector = mySQLConnector;
	}

	@Override
	public void executeStatement(String statement) {
		checkConnection();

		PreparedStatement preparedStatement;
		try {
			preparedStatement = mySQLConnector.getConnection().prepareStatement(statement);
			preparedStatement.executeUpdate(statement);
			
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (Exception exception) {
			System.out.println("Error happended: " + exception.getMessage());
			System.out.println("Exception: " + exception.toString());
		}
	}

	@Override
	public boolean connectMySQL() {
		if(this.mySQLConnector != null){
			return this.mySQLConnector.connect();
		}
		return false;
	}

	@Override
	public boolean disconnectMySQL() {
		if(this.mySQLConnector != null && this.mySQLConnector.isConnected()){
			return this.mySQLConnector.close();
		}
		return false;
	}

	@Override
	public boolean isMySQLConnected() {
		return this.mySQLConnector != null ? this.mySQLConnector.isConnected() : false;
	}

	@Override
	public ResultSet executeQuery(String query) {
		checkConnection();

		try {
			PreparedStatement preparedStatement = mySQLConnector.getConnection().prepareStatement(query);
			return preparedStatement.executeQuery(query);
		} catch (SQLException sqlException) {
			System.out.println("Error happended: " + sqlException.getMessage());
			System.out.println("Exception: " + sqlException.toString());
			return null;
		}
	}

	@Override
	public boolean closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private void checkConnection(){
		try {
			if (this.mySQLConnector.getConnection() == null || this.mySQLConnector.getConnection().isValid(0) == false) {
				connectMySQL();
			}
		} catch (Exception exception){
			System.out.println(exception.getMessage());
		}
	}
}
