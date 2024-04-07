package net.items.store.minigames.core.team.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent){
        Player player = playerInteractEvent.getPlayer();

        if(playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR
                || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(playerInteractEvent.getItem() != null && playerInteractEvent.getItem().getItemMeta() != null
                    && playerInteractEvent.getItem().getItemMeta().getDisplayName() != null){
                ItemStack itemStack = playerInteractEvent.getItem();

                ITeamManager teamManager = MiniGame.get(TeamManager.class);
                if(teamManager.compareItems(itemStack)){
                    teamManager.openTeamInventory(player);
                }
            }
        }
    }
}
