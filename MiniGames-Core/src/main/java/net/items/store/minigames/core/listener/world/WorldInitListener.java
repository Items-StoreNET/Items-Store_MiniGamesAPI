package net.items.store.minigames.core.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldInitListener implements Listener {

    @EventHandler
    public void onWorldInit(WorldInitEvent worldInitEvent){
        worldInitEvent.getWorld().setKeepSpawnInMemory(false);
    }

}
