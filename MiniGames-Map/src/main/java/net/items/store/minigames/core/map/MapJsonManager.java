package net.items.store.minigames.core.map;

import com.google.common.collect.Lists;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.location.GameLocation;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.api.map.IMapJsonManager;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.data.FileBuilder;
import net.items.store.minigames.core.map.location.LocationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapJsonManager implements IMapJsonManager {

    private final String MAP_JSON_FILE_NAME = "maps.json";

    public MapJsonManager() { }

    @Override
    public List<GameMap> loadMaps() {
        IMapManager mapManager = MiniGame.get(MapManager.class);
        ILocationManager locationManager = MiniGame.get(LocationManager.class);
        ITeamManager teamManager = MiniGame.get(ITeamManager.class);

        JSONArray jsonArray = FileBuilder.loadJSONArray(MAP_JSON_FILE_NAME, false);
        List<GameMap> gameMapList = Lists.newArrayList();

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String mapName = jsonObject.get("mapName").toString();
            Material mapMaterial = Material.valueOf(jsonObject.get("mapMaterial").toString().toUpperCase());

            String worldName = "";

            for (String key : mapManager.getLocationStateMap().keySet()){
                GameLocation gameLocation = null;

                switch (mapManager.getLocationStateMap().get(key)){
                    case NORMAL:
                        gameLocation = locationManager.getGameLocation(key, LocationState.NORMAL, mapName);
                        break;
                    case TEAM:
                        List<GameTeam> gameTeamList = teamManager.getTeams();

                        gameLocation = locationManager.getGameLocation(key, LocationState.TEAM, mapName,
                                gameTeamList.get(0).getTeamName());
                        break;
                    case COUNT:
                        List<GameLocation> gameLocationList = locationManager.getGameLocations(key, LocationState.COUNT, mapName);
                        gameLocation = gameLocationList.size() > 0 ? gameLocationList.get(0) : null;
                        break;
                }

                if (gameLocation != null){
                    worldName = gameLocation.getWorldName();
                    break;
                }
            }

            GameMap gameMap = new GameMap(mapName, mapMaterial, worldName);
            gameMapList.add(gameMap);
        }

        return gameMapList;
    }

    @Override
    public void loadMapLocations(){
        IMapManager mapManager = MiniGame.get(IMapManager.class);
        ILocationManager locationManager = MiniGame.get(ILocationManager.class);
        ITeamManager teamManager = MiniGame.get(ITeamManager.class);

        for (GameMap gameMap : mapManager.getMaps()){
            for(String key : mapManager.getLocationStateMap().keySet()){
                switch (mapManager.getLocationStateMap().get(key)){
                    case NORMAL:
                        gameMap.addLocation
                        (
                            locationManager.getReplacedIdentifier(key, LocationState.NORMAL, gameMap.getMapName()),
                            locationManager.getLocation(key, LocationState.NORMAL, gameMap.getMapName())
                        );
                        break;
                    case TEAM:
                        List<GameTeam> gameTeamList = teamManager.getTeams();
                        for (GameTeam gameTeam : gameTeamList){
                            gameMap.addLocation
                            (
                                locationManager.getReplacedIdentifier(key, LocationState.TEAM, gameMap.getMapName(), gameTeam.getTeamName()),
                                locationManager.getLocation(key, LocationState.TEAM, gameMap.getMapName(), gameTeam.getTeamName())
                            );
                        }
                        break;
                    case COUNT:
                        List<Location> locationList = locationManager
                                .getLocations(key, LocationState.COUNT, gameMap.getMapName());
                        gameMap.addLocation
                                (
                                        locationManager.getReplacedIdentifier(key, LocationState.COUNT, gameMap.getMapName()),
                                        locationList
                                );
                        break;
                }
            }
        }
    }

    @Override
    public void addGameMapToJson(GameMap gameMap) {
        File file = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), MAP_JSON_FILE_NAME).toFile();
        JSONArray jsonArray = FileBuilder.loadJSONArray(MAP_JSON_FILE_NAME, false);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mapName", gameMap.getMapName());
            jsonObject.put("mapMaterial", gameMap.getMapMaterial().toString());
            jsonArray.add(jsonObject);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write(jsonArray.toJSONString());
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGameMapsToJson(List<GameMap> gameMapList) {
        File file = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), MAP_JSON_FILE_NAME).toFile();
        JSONArray jsonArray = new JSONArray();

        try {
            for (GameMap gameMap : gameMapList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mapName", gameMap.getMapName());
                jsonObject.put("mapMaterial", gameMap.getMapMaterial().toString());
                jsonArray.add(jsonObject);
            }

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                writer.write(jsonArray.toJSONString());
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
