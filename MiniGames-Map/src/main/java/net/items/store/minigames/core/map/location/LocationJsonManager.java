package net.items.store.minigames.core.map.location;

import com.google.common.collect.Lists;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.location.GameLocation;
import net.items.store.minigames.api.location.ILocationJsonManager;
import net.items.store.minigames.core.data.FileBuilder;
import org.bukkit.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class LocationJsonManager implements ILocationJsonManager {

    private final String LOCATION_JSON_FILE_NAME = "locations.json";

    public LocationJsonManager(){}

    @Override
    public List<GameLocation> loadLocations() {
        JSONArray jsonArray = FileBuilder.loadJSONArray(LOCATION_JSON_FILE_NAME, false);
        return loadGameLocationListFromJsonObject(jsonArray);
    }

    @Override
    public void addLocationToJson(String identifier, Location location) {
        File file = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), LOCATION_JSON_FILE_NAME).toFile();
        JSONArray jsonArray = FileBuilder.loadJSONArray(LOCATION_JSON_FILE_NAME, false);

        try {
            JSONObject jsonObject = getJSONObjectFromIdentifierAndLocation(identifier, location);
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
    public void updateLocationToJson(String identifier, Location oldLocation, Location newLocation) {
        File file = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), LOCATION_JSON_FILE_NAME).toFile();
        JSONArray jsonArray = FileBuilder.loadJSONArray(LOCATION_JSON_FILE_NAME, false);

        try {
            JSONObject jsonObject = getJSONObjectFromIdentifierAndLocation(identifier, oldLocation);
            JSONObject searchingJSONObject = (JSONObject) jsonArray.stream()
                    .filter(x -> compareJSONObjects(jsonObject, (JSONObject) ((JSONObject)x).clone()))
                    .findAny().orElse(null);

            if(searchingJSONObject != null){
                jsonArray.remove(searchingJSONObject);
            }
            jsonArray.add(getJSONObjectFromIdentifierAndLocation(identifier, newLocation));

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

    private boolean compareJSONObjects(JSONObject firstJSONObject, JSONObject secondJSONObject){
        return firstJSONObject.toJSONString().equalsIgnoreCase(secondJSONObject.toJSONString());
    }

    private JSONObject getJSONObjectFromIdentifierAndLocation(String identifier, Location location){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("identifier", identifier);
        jsonObject.put("worldName", location.getWorld().getName());
        jsonObject.put("x", location.getX());
        jsonObject.put("y", location.getY());
        jsonObject.put("z", location.getZ());
        jsonObject.put("yaw", location.getYaw());
        jsonObject.put("pitch", location.getPitch());
        return jsonObject;
    }

    private List<GameLocation> loadGameLocationListFromJsonObject(JSONArray jsonArray) {
        List<GameLocation> gameLocationList = Lists.newArrayList();

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String identifier = jsonObject.get("identifier").toString();
            String worldName = jsonObject.get("worldName").toString();

            double x = Double.valueOf(jsonObject.get("x").toString());
            double y = Double.valueOf(jsonObject.get("y").toString());
            double z = Double.valueOf(jsonObject.get("z").toString());
            double yaw = Double.valueOf(jsonObject.get("yaw").toString());
            double pitch = Double.valueOf(jsonObject.get("pitch").toString());

            Location location = new Location(null, x, y, z);
            location.setYaw((float) yaw);
            location.setPitch((float) pitch);

            GameLocation gameLocation = new GameLocation(identifier, worldName, location);
            gameLocationList.add(gameLocation);
        }
        return gameLocationList;
    }
}
