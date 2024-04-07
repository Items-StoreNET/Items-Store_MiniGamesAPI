package net.items.store.minigames.core.listener.player;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.core.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent playerDropItemEvent){
        Player player = playerDropItemEvent.getPlayer();

        if(MiniGame.get(PlayerManager.class).canPlayerBuild(player.getUniqueId()) == false){
            if(MiniGame.get(AbstractGame.class).getGameInteraction().isDropAble() == false){
                playerDropItemEvent.setCancelled(true);
            }
        }
    }
}
