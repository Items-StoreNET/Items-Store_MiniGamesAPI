package net.items.store.minigames.core.stats;

import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.stats.AbstractStats;
import net.items.store.minigames.api.stats.IStatsManager;
import net.items.store.minigames.core.mysql.AbstractSQLDataTable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class StatsManager<T> implements IStatsManager<T> {

    private Map<UUID, T> cachedStatsMap;
    private Map<UUID, Map.Entry<Long, Integer>> topStatsMap;
    private Map.Entry<Long, List<Map.Entry<UUID, Integer>>> topTenStatsCache;

    public StatsManager() {
        this.cachedStatsMap = Maps.newHashMap();
        this.topTenStatsCache = null;
        this.topStatsMap = Maps.newHashMap();
    }

    @Override
    public int loadRank(UUID uniqueID) {
        AbstractSQLDataTable abstractSQLDataTable = MiniGame.get(AbstractSQLDataTable.class);

        if (topStatsMap.containsKey(uniqueID)){
            Map.Entry<Long, Integer> entry = topStatsMap.get(uniqueID);

            if (System.currentTimeMillis() - entry.getKey() < 30000){
                return entry.getValue();
            }
        }

        int ranking = abstractSQLDataTable.loadRank(uniqueID);
        topStatsMap.put(uniqueID, new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), ranking));
        return ranking;
    }

    @Override
    public List<Map.Entry<UUID, Integer>> loadTopTen() {
        AbstractSQLDataTable abstractSQLDataTable = MiniGame.get(AbstractSQLDataTable.class);

        if (topTenStatsCache != null && (System.currentTimeMillis() - topTenStatsCache.getKey()) < 60000){
            return topTenStatsCache.getValue();
        }

        List<Map.Entry<UUID, Integer>> topTen = abstractSQLDataTable.loadTopTen();
        topTenStatsCache = new AbstractMap.SimpleEntry<>(System.currentTimeMillis(), topTen);
        return topTen;
    }

    @Override
    public void createPlayer(UUID uniqueID) {
        AbstractSQLDataTable<T> abstractSQLDataTable = MiniGame.get(AbstractSQLDataTable.class);
        abstractSQLDataTable.createPlayer(uniqueID);
    }

    @Override
    public T loadPlayerData(UUID uniqueID) {
        if(this.cachedStatsMap.containsKey(uniqueID)){
            return this.cachedStatsMap.get(uniqueID);
        }

        AbstractSQLDataTable<T> abstractSQLDataTable = MiniGame.get(AbstractSQLDataTable.class);
        T playerData = abstractSQLDataTable.getPlayerData(uniqueID);

        if (playerData != null){
            this.cachedStatsMap.put(uniqueID, playerData);
        }

        return playerData;
    }

    @Override
    public void savePlayerData(T stats) {
        MiniGame.getExecutorService().submit(() -> {
            AbstractSQLDataTable abstractSQLDataTable = MiniGame.get(AbstractSQLDataTable.class);
            abstractSQLDataTable.updatePlayerData(stats);
        });
    }

    @Override
    public boolean removeFromCachedData(UUID uniqueID) {
        if(this.cachedStatsMap.containsKey(uniqueID)){
            this.cachedStatsMap.remove(uniqueID);
            return true;
        }
        return false;
    }

    @Override
    public List<T> getCachedData() {
        return this.cachedStatsMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public void clearCachedData() {
        this.cachedStatsMap.clear();
    }

    @Override
    public Object getDataObject(UUID uniqueID, String identifier) {
        AbstractStats abstractStats = (AbstractStats) loadPlayerData(uniqueID);

        if(abstractStats == null){
            return null;
        }

        return abstractStats.getStatsObject(identifier);
    }

    @Override
    public List<String> getDataStringList(UUID uniqueID) {
        AbstractStats abstractStats = (AbstractStats) loadPlayerData(uniqueID);

        if(abstractStats == null){
            return null;
        }

        return abstractStats.getStatsStringList();
    }

    @Override
    public void registerDefault() {
    }
}
