package net.items.store.minigames.core.kit;

import com.google.common.collect.Lists;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.api.kit.PlayerKit;
import net.items.store.minigames.api.sql.IMySQL;
import net.items.store.minigames.api.sql.SQLDataColumn;
import net.items.store.minigames.api.sql.SQLDataSetting;
import net.items.store.minigames.api.sql.SQLDataType;
import net.items.store.minigames.core.mysql.AbstractSQLDataTable;
import net.items.store.minigames.core.mysql.MySQL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerKitTable extends AbstractSQLDataTable<PlayerKit> {

    public PlayerKitTable(IMySQL mySQL) {
        super(mySQL, "PlayerKits");
    }

    @Override
    protected void registerTableColumns() {
        this.tableColumnsList.add(new SQLDataColumn(COL_UNIQUEID, SQLDataType.VARCHAR, 100, SQLDataSetting.UNIQUE));
        this.tableColumnsList.add(new SQLDataColumn(COL_KITS, SQLDataType.TEXT));
    }

    public void createPlayer(UUID uniqueID){
        MiniGame.getExecutorService().submit(() -> {
            ResultSet resultSet = findPlayerResultSet(COL_UNIQUEID + " = '" + uniqueID.toString() + "'");

            try {
                if (resultSet == null || resultSet.next() == false || resultSet.wasNull() == true) {
                    IKitManager kitManager = MiniGame.get(KitManager.class);
                    Kit kit = kitManager.getDefaultKit();
                    JSONArray jsonArray = new JSONArray();
                    if(kit != null){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("kitName", kit.getKitName());
                        jsonObject.put("active", true);
                        jsonArray.add(jsonObject);
                    }

                    executeInsert(uniqueID, jsonArray.toJSONString());
                }
            } catch (Exception exception){
                System.out.println(exception.getMessage());
            } finally {
                this.mySQL.closeResultSet(resultSet);
            }
        });
    }

    @Override
    public PlayerKit getPlayerData(UUID uniqueID) {
        return null;
    }

    @Override
    public void updatePlayerData(PlayerKit data) {

    }

    @Override
    public List<Map.Entry<UUID, Integer>> loadTopTen() {
        return null;
    }

    public List<PlayerKit> getPlayerKits(UUID uniqueID){
        MySQL mySQL = MiniGame.get(MySQL.class);
        List<PlayerKit> playerKitList = Lists.newArrayList();

        if(mySQL.isMySQLConnected()){
            JSONParser jsonParser = new JSONParser();
            IKitManager kitManager = MiniGame.get(KitManager.class);
            ResultSet resultSet = findPlayerResultSet(COL_UNIQUEID + " = '" + uniqueID.toString() + "'");

            try {
                if(resultSet != null && resultSet.next() && !resultSet.wasNull()){
                    JSONArray jsonArray = (JSONArray) jsonParser.parse(resultSet.getString("Kits"));

                    for(int i = 0; i < jsonArray.size(); i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String kitName = jsonObject.get("kitName").toString();
                        Kit kit = kitManager.getKitFromName(kitName);
                        boolean active = Boolean.valueOf(jsonObject.get("active").toString());

                        PlayerKit playerKit = new PlayerKit(kit, kitName, active);
                        playerKitList.add(playerKit);
                    }
                }
            } catch (Exception exception){
                exception.printStackTrace();
            } finally {
                mySQL.closeResultSet(resultSet);
            }
        }

        return playerKitList;
    }

    public void savePlayerKits(UUID uniqueID, List<PlayerKit> playerKitList){
        if (mySQL.isMySQLConnected()) {
            MiniGame.getExecutorService().submit(() -> {
                JSONArray jsonArray = new JSONArray();
                for(PlayerKit playerKit : playerKitList){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("kitName", playerKit.getKitName());
                    jsonObject.put("active", playerKit.isActive());
                    jsonArray.add(jsonObject);
                }

                String statement = "UPDATE " + this.getTableName()
                        + " SET " + COL_KITS + " = '" + jsonArray.toJSONString() + "'"
                        + " WHERE " + COL_UNIQUEID + " = '" + uniqueID + "';";

                mySQL.executeStatement(statement);
            });
        }
    }

    private final String COL_UNIQUEID = "UniqueID";
    private final String COL_KITS = "Kits";
}
