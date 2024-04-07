package net.items.store.minigames.core.voting.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.voting.IVotingManager;
import net.items.store.minigames.api.voting.VotingHeader;
import net.items.store.minigames.core.voting.VotingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent){
        Player player = (Player) inventoryClickEvent.getWhoClicked();
        IVotingManager votingManager = MiniGame.get(VotingManager.class);

        if(inventoryClickEvent.getClickedInventory() != null
                && inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null
                && inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null) {
            String inventoryName = inventoryClickEvent.getView().getTitle();
            int inventorySize = inventoryClickEvent.getClickedInventory().getSize();
            ItemStack currentItem = inventoryClickEvent.getCurrentItem();

            if (votingManager.isVotingInventory(inventoryName, inventorySize)){
                inventoryClickEvent.setCancelled(true);

                if(votingManager.clickInventory(player, inventoryName, currentItem, inventorySize)){
                    player.closeInventory();
                }
            }
        }
    }
}
