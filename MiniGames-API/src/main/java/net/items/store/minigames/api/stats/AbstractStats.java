package net.items.store.minigames.api.stats;

import java.util.List;
import java.util.Map;

public abstract class AbstractStats {

    public abstract List<String> getStatsStringList();

    public abstract Map<Object, Object> getStatsObjectMap(int rank);

    public abstract Object getStatsObject(String identifier);

}
