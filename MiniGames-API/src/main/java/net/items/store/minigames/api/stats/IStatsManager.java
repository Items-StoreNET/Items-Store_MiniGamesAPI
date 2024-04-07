package net.items.store.minigames.api.stats;

import net.items.store.minigames.api.IDefaultManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IStatsManager<T> extends IDefaultManager {

    /**
     * Searches for stats in the Database.
     * If there are no stats in the database, they will be created.
     * @param uniqueID of the Player
     * @return an instance of the class PlayerStats
     */
    T loadPlayerData(UUID uniqueID);

    int loadRank(UUID uniqueID);

    List<Map.Entry<UUID, Integer>> loadTopTen();

    void createPlayer(UUID uniqueID);

    /**
     * Saves player stats to the Database
     * @param t
     */
    void savePlayerData(T t);

    boolean removeFromCachedData(UUID uniqueID);

    /**
     * @return All cached stats which have not yet been deleted.
     */
    List<T> getCachedData();

    /**
     * Clearing all cached stats
     */
    void clearCachedData();

    Object getDataObject(UUID uniqueID, String identifier);

    List<String> getDataStringList(UUID uniqueID);

}
