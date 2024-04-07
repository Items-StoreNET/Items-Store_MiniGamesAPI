package net.items.store.minigames.api.map;

import net.items.store.minigames.api.IDefaultManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.team.GameTeam;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface IMapManager extends IDefaultManager {

	void updateGameMaps();

	/**
	 * Add a new Map to the AbstractMap List
	 * @param gameMap
	 */
	void addMap(Player player, GameMap gameMap);

	void addLocationState(String identifier, LocationState locationState);

	void loadMaps();

	void loadMapVoting();

	/**
	 * @return the AbstractMap List
	 */
	List<GameMap> getMaps();

	/**
	 * Take a randomMap from an AbstractMap List and set it to the current map
	 */
	void takeRandomMap();

	/**
	 * @return the current map
	 */
	GameMap getCurrentMap();

	GameMap getMapByName(String mapName);

	//void openMapsInventory(Player player);

	//void openMapBlockInventory(Player player, String mapName);

	//void openMapBlockInventory(Player player, String mapName, int currentSite, MapBlockDirection mapBlockDirection);

	//void openMapLocationInventory(Player player, String mapName);

	//void openMapTeamLocationInventory(Player player, String mapName, String teamName);

	//void openMapCountLocationInventory(Player player, String mapName, String countName);

	void teleportPlayerToLobby(Player player);

	void teleportPlayerToSpectator(Player player);

	/**
	 * Set the current map
	 * @param gameMap
	 */
	void setCurrentMap(GameMap gameMap);

	void setForceMap(GameMap gameMap);

	void loadMap(GameMap gameMap);

	boolean isMapLoaded();

	void deleteOldMaps();

	/**
	 * Teleports the given player to the current map spawn.
	 * @param player
	 */
	void teleportPlayerToCurrentMap(Player player, int spawn);

	void teleportPlayerToCurrentMap(Player player);

	Map<String, LocationState> getLocationStateMap();

	List<String> getLocationStateKeysFromState(LocationState locationState);

}
