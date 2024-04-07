package net.items.store.minigames.core.data;

import com.google.common.collect.Maps;

import java.util.Map;

public class MapBuilder {

    public static Map<Object, Object> getObjectMap(Object[] keys, Object[] values){
        Map<Object, Object> objectObjectMap = Maps.newHashMap();

        for (int i = 0; i < keys.length; i++){
            objectObjectMap.put(keys[i], values[i]);
        }
        return objectObjectMap;
    }

}
