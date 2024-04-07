package net.items.store.minigames.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MiniGame {

    private static final ExecutorService executorService;
    private static JavaPlugin javaPlugin;
    private static List<Object> objectList = Lists.newArrayList();
    private static Map<String, Object> classCacheMap = Maps.newHashMap();

    static {
        executorService = Executors.newCachedThreadPool();
    }

    public static <T> void register(T t){
        objectList.add(t);
    }

    public static void registerDefaults(){
        for(Object object : objectList.stream().collect(Collectors.toList())){
            if(object instanceof IDefaultManager){
                IDefaultManager defaultManager = (IDefaultManager) object;
                defaultManager.registerDefault();
            }
        }
    }

    public static <T> T get(Class<T> tClass){
        if(classCacheMap.containsKey(tClass.toString())){
            return tClass.cast(classCacheMap.get(tClass.toString()));
        }

        for(Object object : objectList){
            if(object.getClass() == tClass){
                return tClass.cast(object);
            } else {
                Class currentClass = object.getClass();
                Class newClass = object.getClass();

                while(currentClass.getInterfaces().length > 0 || currentClass.getSuperclass() != null) {
                    currentClass = newClass;

                    if(currentClass == tClass){
                        classCacheMap.put(tClass.toString(), object);
                        return tClass.cast(object);
                    }

                    if(currentClass.getInterfaces().length > 0){
                        newClass = currentClass.getInterfaces()[0];
                    } else if(currentClass.getSuperclass() != null){
                        newClass = currentClass.getSuperclass();
                    }
                }
            }
        }
        classCacheMap.put(tClass.toString(), null);
        return null;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static void setJavaPlugin(JavaPlugin javaPlugin) {
        MiniGame.javaPlugin = javaPlugin;
    }

    public static JavaPlugin getJavaPlugin() {
        return MiniGame.javaPlugin;
    }
}
