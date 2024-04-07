package net.items.store.minigames.core.listener.block;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.core.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent){
        Player player = blockBreakEvent.getPlayer();

        if(MiniGame.get(PlayerManager.class).canPlayerBuild(player.getUniqueId()) == false){
            if(MiniGame.get(AbstractGame.class).getGameInteraction().isBuildAble() == false){
                blockBreakEvent.setCancelled(true);
            }
        }
    }
}
