package net.items.store.minigames.core.map.location;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.location.GameLocation;
import net.items.store.minigames.api.location.ILocationJsonManager;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.MapManager;
import net.items.store.minigames.core.map.inventory.location.DefaultLocationInventory;
import net.items.store.minigames.core.map.inventory.map.DefaultMapInventory;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocationManager implements ILocationManager {

    private List<GameLocation> gameLocationList;
    private ILocationJsonManager locationJsonManager;
    private final List<String> stringWorldList;

    public LocationManager(){
        this.locationJsonManager = new LocationJsonManager();
        this.gameLocationList = Lists.newArrayList();
        this.gameLocationList = locationJsonManager.loadLocations();
        this.stringWorldList = Lists.newArrayList();
    }

    @Override
    public Location getLocation(String identifier, LocationState locationState, Object... variableObjects) {
        String completeIdentifier = getReplacedIdentifier(identifier, locationState, variableObjects);
        Location location = null;
        Optional<GameLocation> optionalGameLocation = this.gameLocationList.stream()
                .filter(x -> x.getIdentifier().equalsIgnoreCase(completeIdentifier)).findFirst();

        if(optionalGameLocation.isPresent()){
            location = optionalGameLocation.get().getLocation();
        }
        return location;
    }

    @Override
    public GameLocation getGameLocation(String identifier, LocationState locationState, Object... variableObjects) {
        String completeIdentifier = getReplacedIdentifier(identifier, locationState, variableObjects);
        Optional<GameLocation> optionalGameLocation = this.gameLocationList.stream()
                .filter(x -> x.getIdentifier().equalsIgnoreCase(completeIdentifier)).findFirst();

        return optionalGameLocation.isPresent() ? optionalGameLocation.get() : null;
    }

    @Override
    public List<Location> getLocations(String identifier, LocationState locationState, Object... variableObjects) {
        String completeIdentifier = getReplacedIdentifier(identifier, locationState, variableObjects);

        List<Location> gameLocationList = this.gameLocationList.stream()
                .filter(x -> x.getIdentifier().equals(completeIdentifier))
                .map(x -> x.getLocation())
                .collect(Collectors.toList());
        return gameLocationList;
    }

    @Override
    public List<GameLocation> getGameLocations(String identifier, LocationState locationState, Object... variableObjects) {
        String completeIdentifier = getReplacedIdentifier(identifier, locationState, variableObjects);

        List<GameLocation> gameLocationList = this.gameLocationList.stream()
                .filter(x -> x.getIdentifier().equals(completeIdentifier))
                .collect(Collectors.toList());
        return gameLocationList;
    }

    @Override
    public void setLocation(Player player, String identifier, Location location, LocationState locationState, Object... variableObjects) {
        String completeIdentifier = getReplacedIdentifier(identifier, locationState, variableObjects);
        IMessageManager messageManager = MiniGame.get(MessageManager.class);

        if(locationState == LocationState.COUNT) {
            GameLocation gameLocation = new GameLocation(completeIdentifier, location.getWorld().getName(), location);

            this.gameLocationList.add(gameLocation);
            this.locationJsonManager.addLocationToJson(completeIdentifier, location);
        } else {
            Optional<GameLocation> optionalGameLocation = this.gameLocationList.stream()
                    .filter(x -> x.getIdentifier().equalsIgnoreCase(completeIdentifier)).findAny();
            if (optionalGameLocation.isPresent() == false) {
                GameLocation gameLocation = new GameLocation(completeIdentifier, location.getWorld().getName(), location);

                this.gameLocationList.add(gameLocation);
                this.locationJsonManager.addLocationToJson(completeIdentifier, location);
            } else {
                GameLocation gameLocation = optionalGameLocation.get();
                Location oldLocation = gameLocation.getLocation().clone();
                gameLocation.setLocation(location);

                this.locationJsonManager.updateLocationToJson(completeIdentifier, oldLocation, location);
            }
        }
        Map<Object, Object> objectObjectMap = Maps.newHashMap();
        objectObjectMap.put("{LOCATIONNAME}", identifier);
        player.sendMessage(messageManager.getMessage("LocationSet", objectObjectMap));
    }

    @Override
    public void openLocationInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§eLocations");
        IMapManager mapManager = MiniGame.get(MapManager.class);

        if(mapManager.getLocationStateMap().values().contains(LocationState.LOBBY)) {
            Location lobbyLocation = getLocation("Lobby", LocationState.LOBBY);
            ItemBuilder itemBuilder = ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§aLobby")
                    .setModifiedBuilder(lobbyLocation, "§6Lobby");
            inventory.setItem(0, itemBuilder.buildItem());
        }
        inventory.setItem(1, ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§aMaps")
                .addLore("").addLore("§7Klicke, um die §6Maps §7zu öffnen.").buildItem());
        player.openInventory(inventory);
    }

    @Override
    public void loadLocations() {
        for (GameLocation gameLocation : this.gameLocationList){
            if (gameLocation.getLocation().getWorld() == null){
                String worldName = gameLocation.getWorldName();

                if(!Bukkit.getWorlds().stream()
                        .map(currentWorld -> currentWorld.getName())
                        .collect(Collectors.toList())
                        .contains(worldName)) {
                    WorldCreator worldCreator = new WorldCreator(worldName);
                    worldCreator.createWorld();
                    Bukkit.createWorld(worldCreator);
                }
                World world = Bukkit.getWorld(worldName);

                gameLocation.getLocation().setWorld(world);

                if(!this.stringWorldList.contains(worldName)){
                    this.stringWorldList.add(worldName);

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
                }
            }
        }
    }

    public String getReplacedIdentifier(String identifier, LocationState locationState, Object... variableObjects){
        Map<Object, Object> objectObjectMap = Maps.newHashMap();

        switch (locationState){
            case LOBBY:
                identifier = identifier + locationState.getIdentifier();
                break;
            case TEAM:
                if(variableObjects.length <= 1){
                    throw new NullPointerException();
                }
                objectObjectMap.put("{MAP]", variableObjects[0].toString());
                objectObjectMap.put("{TEAMNAME}", variableObjects[1].toString());

                identifier = identifier + locationState.getIdentifier(objectObjectMap);
                break;
            case NORMAL:
            case COUNT:
                if(variableObjects.length == 0){
                    throw new NullPointerException("Unkown variable Objects for Identifier " + identifier);
                }
                objectObjectMap.put("{MAP}", variableObjects[0].toString());

                identifier = identifier + locationState.getIdentifier(objectObjectMap);
                break;
        }
        return identifier;
    }

    @Override
    public void registerDefault() {
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("LocationSet","{PREFIX}§7Die Location §e{LOCATIONNAME} §7wurde gesetzt§8.");

        IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
        inventoryManager.addCustomInventory(new DefaultLocationInventory());
    }
}
