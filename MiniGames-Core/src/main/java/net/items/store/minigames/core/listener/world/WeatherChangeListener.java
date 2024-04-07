package net.items.store.minigames.core.listener.world;

import java.util.List;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.google.common.collect.Lists;

public class WeatherChangeListener implements Listener {

    private List<String> stringWorldList = Lists.newArrayList();

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        World world = event.getWorld();
        if((!world.isThundering() && !world.hasStorm()) || stringWorldList.contains(world.getName().toLowerCase())) {
            event.setCancelled(true);
            return;
        }
        stringWorldList.add(world.getName().toLowerCase());

        if(world.isThundering()) {
            world.setThundering(false);
            world.setThunderDuration(Integer.MAX_VALUE);
        }
        if(world.hasStorm()) {
            world.setStorm(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
        }
    }

}