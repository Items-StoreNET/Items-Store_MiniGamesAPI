package net.items.store.minigames.api.location;

import net.items.store.minigames.api.IDefaultManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface ILocationManager extends IDefaultManager {

    /**
     *
     * @param identifier
     * @param locationState
     * @param variableObjects
     * @return
     */
    Location getLocation(String identifier, LocationState locationState, Object... variableObjects);

    GameLocation getGameLocation(String identifier, LocationState locationState, Object... variableObjects);

    String getReplacedIdentifier(String identifier, LocationState locationState, Object... variableObjects);

    /**
     *
     * @param identifier
     * @param locationState
     * @param variableObjects
     * @return
     */
    List<Location> getLocations(String identifier, LocationState locationState, Object... variableObjects);

    List<GameLocation> getGameLocations(String identifier, LocationState locationState, Object... variableObjects);

    /**
     *
     * @param player
     * @param identifier
     * @param location
     * @param locationState
     * @param variableObjects
     */
    void setLocation(Player player, String identifier, Location location, LocationState locationState, Object... variableObjects);

    void openLocationInventory(Player player);

    void loadLocations();

}
