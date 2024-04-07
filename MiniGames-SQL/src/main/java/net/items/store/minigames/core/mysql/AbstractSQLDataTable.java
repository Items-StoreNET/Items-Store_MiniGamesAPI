package net.items.store.minigames.core.mysql;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.items.store.minigames.api.sql.*;

import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractSQLDataTable<T> implements ISQLDataTable {

    @Getter
    private String tableName;

    protected IMySQL mySQL;

    protected List<SQLDataColumn> tableColumnsList;

    public AbstractSQLDataTable(IMySQL mySQL, String tableName){
        this.mySQL = mySQL;
        this.tableName = tableName;
        this.tableColumnsList = Lists.newArrayList();

        registerTableColumns();
        createTable();
    }

    private void createTable(){
        if (this.mySQL.isMySQLConnected()) {
            StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + this.tableName + " (");
            int current = 0;

            for (SQLDataColumn sqlDataColumn : this.tableColumnsList){
                if(current > 0){
                    stringBuilder.append(", ");
                }
                stringBuilder.append(sqlDataColumn.getName());
                stringBuilder.append(" ");
                stringBuilder.append(sqlDataColumn.getSqlDataType().getSqlName());

                if(sqlDataColumn.getSqlDataType() == SQLDataType.VARCHAR){
                    stringBuilder.append("(" + sqlDataColumn.getLength() + ")");
                } else {
                    if (sqlDataColumn.hasSetting(SQLDataSetting.CAN_BE_NULL) == false){
                        stringBuilder.append(" NOT NULL");
                    }

                    if (sqlDataColumn.hasSetting(SQLDataSetting.AUTO_INCREMENT)){
                        stringBuilder.append(" AUTO_INCREMENT");
                    }

                    if (sqlDataColumn.hasSetting(SQLDataSetting.UNIQUE)){
                        stringBuilder.append(" UNIQUE");
                    }
                }
                current++;
            }
            stringBuilder.append(")");
            this.mySQL.executeStatement(stringBuilder.toString());
        }
    }

    protected void executeInsert(Object... objects){
        if (this.mySQL.isMySQLConnected()) {
            StringBuilder stringBuilder = new StringBuilder("INSERT INTO " + this.tableName + " (");
            int current = 0;

            for (SQLDataColumn sqlDataColumn : this.tableColumnsList) {
                if (sqlDataColumn.hasSetting(SQLDataSetting.AUTO_INCREMENT) == false){
                    if(current > 0){
                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(sqlDataColumn.getName());
                    current++;
                }
            }
            stringBuilder.append(") VALUES ('");
            stringBuilder.append(String.join("','",
                    Lists.newArrayList(objects).stream().map(x -> x.toString()).collect(Collectors.toList())));
            stringBuilder.append("');");

            this.mySQL.executeStatement(stringBuilder.toString());
        }
    }

    protected ResultSet findPlayerResultSet(String where){
        return findPlayerResultSet(where, "");
    }

    protected ResultSet findPlayerResultSet(String where, String orderBy){
        ResultSet resultSet = null;

        try {
            String sqlString = "SELECT * FROM " + this.tableName;

            if (where.equalsIgnoreCase("") == false){
                sqlString = sqlString + " WHERE " + where + "";
            }
            if (orderBy.equalsIgnoreCase("") == false){
                sqlString = sqlString + " ORDER BY " + orderBy + "";
            }

            resultSet = this.mySQL.executeQuery(sqlString);
        } catch (Exception exception){
            exception.printStackTrace();
        }

        return resultSet;
    }

    protected abstract void registerTableColumns();

    public abstract void createPlayer(UUID uniqueID);

    public abstract T getPlayerData(UUID uniqueID);

    public abstract void updatePlayerData(T data);

    public int loadRank(Object uniqueValue) {
        int ranking = 0;

        if (mySQL.isMySQLConnected()){
            ResultSet resultSet = null;

            try {
                resultSet = findPlayerResultSet
                    (
                            "",
                            getFirstColumnNameWithSetting(SQLDataSetting.CAN_BE_USED_FOR_ORDER) + " DESC"
                    );

                while (resultSet != null && resultSet.next() && resultSet.wasNull() == false) {
                    ranking += 1;

                    if (resultSet.getString(getFirstColumnNameWithSetting(SQLDataSetting.UNIQUE)).equalsIgnoreCase(uniqueValue.toString())) {
                        break;
                    }
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            } finally {
                if (resultSet != null){
                    mySQL.closeResultSet(resultSet);
                }
            }
        }

        return ranking;
    }

    public List<Map.Entry<UUID, Integer>> loadTopTen() {
        List<Map.Entry<UUID, Integer>> entryList = Lists.newArrayList();

        if (mySQL.isMySQLConnected()){
            ResultSet resultSet = findPlayerResultSet("",
                    getFirstColumnNameWithSetting(SQLDataSetting.CAN_BE_USED_FOR_ORDER) + " DESC LIMIT 10;");

            try {
                int ranking = 0;

                while (resultSet != null && resultSet.next() && resultSet.wasNull() == false){
                    entryList.add(new AbstractMap.SimpleEntry<>
                            (
                                    UUID.fromString(resultSet.getString(getFirstColumnNameWithSetting(SQLDataSetting.UNIQUE))),
                                    ++ranking
                            ));
                }
            } catch (Exception exception){
                System.out.println(exception.getMessage());
            } finally {
                mySQL.closeResultSet(resultSet);
            }
        }

        return entryList;
    }

    private String getFirstColumnNameWithSetting(SQLDataSetting sqlDataSetting){
        return this.tableColumnsList.stream()
                .filter(x -> x.hasSetting(sqlDataSetting))
                .findAny()
                .orElse(new SQLDataColumn(null, SQLDataType.VARCHAR))
                .getName();
    }

}
