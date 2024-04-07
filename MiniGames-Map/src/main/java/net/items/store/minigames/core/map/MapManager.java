package net.items.store.minigames.core.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.*;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.api.voting.IVotingManager;
import net.items.store.minigames.api.voting.VotingDetail;
import net.items.store.minigames.api.voting.VotingHeader;
import net.items.store.minigames.core.data.FileBuilder;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.command.LocationCommand;
import net.items.store.minigames.core.map.command.MapCommand;
import net.items.store.minigames.core.map.command.MapsCommand;
import net.items.store.minigames.core.map.inventory.map.*;
import net.items.store.minigames.core.map.listener.InventoryClickListener;
import net.items.store.minigames.core.map.location.LocationManager;
import net.items.store.minigames.core.message.MessageManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class MapManager implements IMapManager {

    private List<GameMap> gameMapList;
    private GameMap currentGameMap;
    private GameMap currentForceMap;
    @Getter
    private IMapJsonManager mapJsonManager;
    private Map<String, LocationState> locationStateMap;
    private VotingHeader votingHeader;
    private VotingDetail votingDetail;
    private boolean mapLoaded;

    public MapManager(){
        this.mapJsonManager = new MapJsonManager();
        this.locationStateMap = Maps.newHashMap();
        this.gameMapList = Lists.newArrayList();
        this.currentGameMap = null;
        this.currentForceMap = null;
        this.mapLoaded = false;
    }

    @Override
    public void updateGameMaps() {
        this.mapJsonManager.updateGameMapsToJson(this.gameMapList);
    }

    @Override
    public void addMap(Player player, GameMap gameMap) {
        Optional<GameMap> optionalGameMap = gameMapList.stream()
                .filter(x -> x.getMapName().equalsIgnoreCase(gameMap.getMapName())).findFirst();

        Map<Object, Object> objectObjectMap = Maps.newHashMap();
        objectObjectMap.put("{MAP}", gameMap.getMapName());

        IMessageManager messageManager = MiniGame.get(MessageManager.class);

        if(!optionalGameMap.isPresent()){
            this.gameMapList.add(gameMap);
            this.mapJsonManager.addGameMapToJson(gameMap);

            player.sendMessage(messageManager.getMessage("AddedMap", objectObjectMap));
        } else {
            player.sendMessage(messageManager.getMessage("MapAlreadyExists", objectObjectMap));
        }
    }

    @Override
    public void addLocationState(String identifier, LocationState locationState) {
        this.locationStateMap.put(identifier, locationState);
    }

    @Override
    public void loadMaps() {
        this.gameMapList = this.mapJsonManager.loadMaps();
    }

    @Override
    public void loadMapVoting() {
        IVotingManager votingManager = MiniGame.get(IVotingManager.class);

        if(votingManager != null){
            JSONObject jsonObject = FileBuilder.loadJSONObject("mapVoting.json", true);
            JSONObject inventoryJsonObject = (JSONObject) jsonObject.get("mapVotingInventory");
            JSONObject itemJsonObject = (JSONObject) jsonObject.get("mapVotingItem");

            String votingIdentifier = "Map";
            String inventoryName = inventoryJsonObject.get("inventoryName").toString();
            int inventorySize = Integer.valueOf(inventoryJsonObject.get("inventorySize").toString());
            ItemStack playerItem = ItemBuilder.modify()
                    .setMaterial(Material.valueOf(itemJsonObject.get("material").toString()))
                    .setDisplayName(itemJsonObject.get("displayName").toString()).buildItem();
            int playerItemSlot = 4;

            votingHeader = new VotingHeader(votingIdentifier, inventoryName, inventorySize, playerItem, playerItemSlot);
            votingDetail = new VotingDetail(votingHeader, "Map", "", null, 0);
            votingHeader.getVotingDetailList().add(votingDetail);

            votingManager.addVoting(votingHeader);
        }

        if(this.votingHeader != null && this.votingDetail != null){
            List<GameMap> currentGameMapList = this.gameMapList.stream().collect(Collectors.toList());
            Random random = new Random();
            IMessageManager messageManager = MiniGame.get(MessageManager.class);
            Map<Object, Object> objectObjectMap = Maps.newHashMap();

            for(int i = 0; i < 3; i++){
                if(currentGameMapList.size() > 0){
                    GameMap gameMap = currentGameMapList.remove(random.nextInt(currentGameMapList.size()));
                    objectObjectMap.put("{MAP}", gameMap.getMapName());

                    String identifier = gameMap.getMapName();
                    ItemBuilder itemBuilder = ItemBuilder.modify()
                            .setMaterial(gameMap.getMapMaterial())
                            .setDisplayName(messageManager.getMessage("MapVotingDisplayName", objectObjectMap));
                    int itemSlot = 0;

                    this.votingDetail.addToVotingTrailer(identifier, itemBuilder, itemSlot);
                }
            }
        }
    }

    @Override
    public List<GameMap> getMaps() {
        return this.gameMapList;
    }

    @Override
    public void takeRandomMap() {
        if(this.gameMapList.size() <= 0){
            System.out.println("ERROR!: Cannot take Random Map because no Maps exist.");
            return;
        }
        Random random = new Random();
        int next = random.nextInt(this.gameMapList.size());

        this.currentGameMap = this.gameMapList.get(next);
    }

    @Override
    public GameMap getCurrentMap() {
        return currentForceMap != null ? currentForceMap : currentGameMap;
    }

    @Override
    public GameMap getMapByName(String mapName) {
        GameMap gameMap = null;

        for(GameMap currentGameMap : this.gameMapList){
            if(currentGameMap.getMapName().equalsIgnoreCase(mapName)){
                gameMap = currentGameMap;
                break;
            }
        }
        return gameMap;
    }

    @Override
    public void teleportPlayerToLobby(Player player) {
        ILocationManager locationManager = MiniGame.get(LocationManager.class);
        Location location = locationManager.getLocation("Lobby", LocationState.LOBBY);

        if(location != null){
            player.teleport(location);
        }
    }

    @Override
    public void setCurrentMap(GameMap gameMap) {
        this.currentGameMap = gameMap;
    }

    @Override
    public void setForceMap(GameMap gameMap) {
        this.currentForceMap = gameMap;
    }

    @Override
    public void teleportPlayerToCurrentMap(Player player, int spawn) {
        ILocationManager locationManager = MiniGame.get(LocationManager.class);

        if(locationStateMap.containsKey("Spawn") == false){
            throw new NullPointerException("Location Identifier 'Spawn' not found.");
        }

        String mapName = getCurrentMap().getMapName();
        List<Location> locationList = locationManager.getLocations("Spawn", LocationState.COUNT, mapName);
        if(locationList.size() < spawn){
            throw new IndexOutOfBoundsException("Spawn '" + spawn + "' is not in Range.");
        }

        Location location = locationManager.getLocations("Spawn", LocationState.COUNT, mapName).get(spawn);
        if(location != null){
            player.teleport(location);
        }
    }

    @Override
    public void teleportPlayerToSpectator(Player player) {
        ILocationManager locationManager = MiniGame.get(LocationManager.class);
        Location location = locationManager.getLocation("Spectator", LocationState.NORMAL);

        if(location != null){
            player.teleport(location);
        }
    }

    @Override
    public boolean isMapLoaded() {
        return mapLoaded;
    }

    @Override
    public void loadMap(GameMap gameMap) {
        try {
            int count = 0;

            if (getCurrentMap() == null) {
                takeRandomMap();
            }

            generateMap(gameMap);

            while (isMapLoaded() == false) {
                Thread.sleep(99);
                count += 1;

                if (count >= 100) {
                    break;
                }
            }

            ITeamManager teamManager = MiniGame.get(ITeamManager.class);
            if (teamManager != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    teamManager.randomTeamForPlayer(player);
                    teleportPlayerToCurrentMap(player);
                }
            } else if (teamManager == null) {
                int spawn = 0;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    teleportPlayerToCurrentMap(player, spawn++);
                }
            }
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    private void generateMap(GameMap gameMap){
        if (gameMap == null){
            System.out.println("Es konnte keine Map gefunden werden, breche ab.");
            return;
        }

        String basePath = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath())
                .getParent().getParent().toString();
        Optional<List<Location>> optionalLocations = gameMap.getLocationList().values().stream()
                .filter(x -> x.size() > 0).findAny();

        if (optionalLocations.isPresent() == false){
            System.out.println("optionalLocations is not present");
            return;
        }

        Optional<Location> optionalLocation = optionalLocations.isPresent()
                ? optionalLocations.get().stream().findAny() : null;

        if (optionalLocation == null || optionalLocation.isPresent() == false){
            System.out.println("optionalLocation is not present or null");
            return;
        }

        String worldName = optionalLocation != null && optionalLocation.isPresent()
                ? optionalLocation.get().getWorld().getName() : null;

        if (worldName == null){
            System.out.println("Es konnte keine World gefunden werden, breche ab.");
            return;
        }

        try {
            FileUtils.copyDirectory
                    (
                            Paths.get(basePath, worldName).toFile(),
                            Paths.get(basePath, worldName + "_InGame_Map").toFile()
                    );

            Path uidPath = Paths.get(basePath, worldName + "_InGame_Map", "uid.dat");
            if (Files.exists(uidPath)){
                FileUtils.delete(uidPath.toFile());
            }
        } catch (Exception exception){
            System.out.println("Cannot copy File: " + exception.getMessage());
        }

        Bukkit.getScheduler().callSyncMethod(MiniGame.getJavaPlugin(), () ->{
            if(!Bukkit.getWorlds().stream()
                    .map(currentWorld -> currentWorld.getName())
                    .collect(Collectors.toList())
                    .contains(worldName + "_InGame_Map")) {
                WorldCreator worldCreator = new WorldCreator(worldName + "_InGame_Map");
                worldCreator.createWorld();
                Bukkit.createWorld(worldCreator);
            }

            World world = Bukkit.getWorld(worldName + "_InGame_Map");
            world.setThundering(false);
            world.setStorm(false);
            world.setAutoSave(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
            world.setThunderDuration(Integer.MAX_VALUE);
            world.setTime(1000);
            world.setDifficulty(Difficulty.NORMAL);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

            for (String key : gameMap.getLocationList().keySet()){
                List<Location> locationList = gameMap.getLocationList().get(key);

                for (Location location : locationList){
                    location.setWorld(world);
                }
            }
            this.mapLoaded = true;
            return null;
        });
    }

    @Override
    public void teleportPlayerToCurrentMap(Player player) {
        ITeamManager teamManager = MiniGame.get(ITeamManager.class);

        if(teamManager != null){
            GameMap currentMap = getCurrentMap();

            ILocationManager locationManager = MiniGame.get(LocationManager.class);
            GameTeam gameTeam = teamManager.getPlayerTeam(player);
            String mapName = getCurrentMap().getMapName();
            String locationIdentifier = locationManager.getReplacedIdentifier("Spawn",
                    LocationState.TEAM, mapName, gameTeam.getTeamName());

            if (currentMap.getLocationList().containsKey(locationIdentifier)){
                Optional<Location> optionalLocation = currentMap.getLocationList()
                        .get(locationIdentifier).stream().findAny();

                if(optionalLocation != null && optionalLocation.isPresent()){
                    Bukkit.getScheduler().callSyncMethod(MiniGame.getJavaPlugin(), () ->{
                        player.teleport(optionalLocation.get());
                        return null;
                    });
                }
            }
        }
    }

    @Override
    public Map<String, LocationState> getLocationStateMap() {
        return locationStateMap;
    }

    @Override
    public List<String> getLocationStateKeysFromState(LocationState locationState) {
        List<String> stringList = Lists.newArrayList();

        for(String key : this.locationStateMap.keySet()){
            if(this.locationStateMap.get(key) == locationState){
                stringList.add(key);
            }
        }
        return stringList;
    }

    @Override
    public void registerDefault() {
        PluginManager pluginManager = MiniGame.getJavaPlugin().getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), MiniGame.getJavaPlugin());

        MiniGame.getJavaPlugin().getCommand("location").setExecutor(new LocationCommand());
        MiniGame.getJavaPlugin().getCommand("map").setExecutor(new MapCommand());
        MiniGame.getJavaPlugin().getCommand("maps").setExecutor(new MapsCommand());

        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("AddedMap", "{PREFIX}§7Die Map §e{MAP} §7wurde §ahinzugefügt§8.");
        messageManager.addMessage("UpdatedBlock", "{PREFIX}§7Der Block für die Map §e{MAP} §7wurde §ageupdated§8.");
        messageManager.addMessage("MapAlreadyExists",
                "{PREFIX}§cDie Map §e{MAP} §ckonnte nicht hinzugefügt werden, da sie bereits existiert.");
        messageManager.addMessage("MapVotingDisplayName", "§e{MAP}");

        IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
        inventoryManager.addCustomInventory(new DefaultMapInventory());
        inventoryManager.addCustomInventory(new MapBlockInventory());
        inventoryManager.addCustomInventory(new MapCountInventory());
        inventoryManager.addCustomInventory(new MapLocationInventory());
        inventoryManager.addCustomInventory(new MapTeamLocationInventory());
    }

    @Override
    public void deleteOldMaps() {
        String basePath = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath())
                .getParent().getParent().toString();

        System.out.println(basePath);

        for (GameMap gameMap : this.gameMapList){
            String worldName = gameMap.getWorldName();
            Path path = Paths.get(basePath, worldName + "_InGame_Map");

            System.out.println("File exists: " + path.toString());


            if (Files.exists(path)){
                System.out.println("Deleting Directory: " + path.toString());
                try {
                    FileUtils.deleteDirectory(path.toFile());
                    System.out.println("Deleted Directory: " + path.toString());
                } catch (Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
        }
    }
}
