package net.items.store.minigames.api.map;

import java.util.List;

public interface IMapJsonManager {

    /**
     *
     */
    List<GameMap> loadMaps();

    void loadMapLocations();

    /**
     *
     * @param gameMap
     */
    void addGameMapToJson(GameMap gameMap);

    void updateGameMapsToJson(List<GameMap> gameMapList);

}
