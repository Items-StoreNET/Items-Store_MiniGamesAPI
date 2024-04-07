package net.items.store.minigames.core.kit.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.core.kit.KitManager;
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

        /*if(playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR
                || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(playerInteractEvent.getItem() != null && playerInteractEvent.getItem().getItemMeta() != null
                    && playerInteractEvent.getItem().getItemMeta().getDisplayName() != null){
                ItemStack itemStack = playerInteractEvent.getItem();
                IKitManager kitManager = MiniGame.get(KitManager.class);

                if(kitManager.compareItems(itemStack)){
                    kitManager.openKitInventory(player);
                }
            }
        }*/
    }
}
