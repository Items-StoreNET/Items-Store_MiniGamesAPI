package net.items.store.minigames.api.location;

import org.bukkit.Location;

import java.util.List;

public interface ILocationJsonManager {

    /**
     *
     */
    List<GameLocation> loadLocations();

    /**
     *
     * @param identifier
     * @param location
     */
    void addLocationToJson(String identifier, Location location);

    void updateLocationToJson(String identifier, Location oldLocation, Location newLocation);

}
