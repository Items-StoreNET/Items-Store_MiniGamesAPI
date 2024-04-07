package net.items.store.minigames.core.coin;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.coin.UserCoins;
import net.items.store.minigames.api.sql.IMySQL;
import net.items.store.minigames.api.sql.SQLDataColumn;
import net.items.store.minigames.api.sql.SQLDataSetting;
import net.items.store.minigames.api.sql.SQLDataType;
import net.items.store.minigames.core.mysql.AbstractSQLDataTable;
import net.items.store.minigames.core.mysql.MySQL;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CoinTable extends AbstractSQLDataTable<UserCoins> {

    public CoinTable(IMySQL mySQL) {
        super(mySQL, "PlayerCoins");
    }

    @Override
    protected void registerTableColumns() {
        this.tableColumnsList.add(new SQLDataColumn("UniqueID", SQLDataType.VARCHAR, 100, SQLDataSetting.UNIQUE));
        this.tableColumnsList.add(new SQLDataColumn("Coins", SQLDataType.LONG));
    }

    public void createPlayer(UUID uniqueID){
        MiniGame.getExecutorService().submit(() -> {
            ResultSet resultSet = findPlayerResultSet("UniqueID = '" + uniqueID.toString() + "'");

            try {
                if (resultSet == null || resultSet.next() == false || resultSet.wasNull() == true) {
                    executeInsert(uniqueID, 0);
                }
            } catch (Exception exception){
                System.out.println(exception.getMessage());
            } finally {
                this.mySQL.closeResultSet(resultSet);
            }
        });
    }

    @Override
    public UserCoins getPlayerData(UUID uniqueID) {
        if(this.mySQL.isMySQLConnected()){
            ResultSet resultSet = findPlayerResultSet("UniqueID = '" + uniqueID.toString() + "'");
            int coins = 0;

            try {
                if(resultSet != null && resultSet.next() && !resultSet.wasNull()){
                    coins = resultSet.getInt("Coins");
                }
                if(resultSet != null){
                    this.mySQL.closeResultSet(resultSet);
                }
            } catch (Exception exception){
                exception.printStackTrace();
            }

            return new UserCoins(uniqueID, coins);
        }
        return new UserCoins(uniqueID, 0);
    }

    @Override
    public void updatePlayerData(UserCoins data) {
        if(this.mySQL.isMySQLConnected()){
            MiniGame.getExecutorService().submit(() -> {
                String statement = "UPDATE PlayerCoins SET Coins= '" + data.getCoinAmount() + "' WHERE UniqueID= '" + data.getUniqueID() + "';";

                this.mySQL.executeStatement(statement);
            });
        }
    }
}
