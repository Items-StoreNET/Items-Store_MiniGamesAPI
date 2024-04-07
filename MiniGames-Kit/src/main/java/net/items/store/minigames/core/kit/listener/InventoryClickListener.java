package net.items.store.minigames.core.kit.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.IPlayerKitManager;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.core.kit.KitManager;
import net.items.store.minigames.core.kit.PlayerKitManager;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent){
        Player player = (Player) inventoryClickEvent.getWhoClicked();

       /* if(inventoryClickEvent.getClickedInventory() != null
            && player.getOpenInventory() != null){
            IKitManager kitManager = MiniGame.get(KitManager.class);
            IPlayerKitManager playerKitManager = MiniGame.get(PlayerKitManager.class);
            IMessageManager messageManager = MiniGame.get(MessageManager.class);

            if(player.getOpenInventory().getTitle().equalsIgnoreCase(kitManager.getInventoryName())){
                inventoryClickEvent.setCancelled(true);

                if(inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null
                    && inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null){
                    Kit kit = kitManager.getKitFromItemStack(inventoryClickEvent.getCurrentItem());

                    if(kit != null){
                        if(playerKitManager.hasKit(player, kit)){
                            kitManager.openUseKitInventory(player, kit);
                        } else {
                            kitManager.openBuyKitInventory(player, kit);
                        }
                    }
                }
            } else {
                Optional<Kit> optionalKit = kitManager.getKits().stream()
                        .filter(x -> player.getOpenInventory().getTitle().contains(x.getKitName())).findFirst();

                if(optionalKit.isPresent()){
                    inventoryClickEvent.setCancelled(true);
                    Kit kit = optionalKit.get();

                    if(inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null
                            && inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null){
                        if(inventoryClickEvent.getCurrentItem().getItemMeta()
                                .getDisplayName().equalsIgnoreCase(messageManager.getMessage("KitBuyDisplayName"))){

                            playerKitManager.buyKit(player, kit);
                            player.closeInventory();

                        } else if(inventoryClickEvent.getCurrentItem().getItemMeta()
                                .getDisplayName().equalsIgnoreCase(messageManager.getMessage("KitChangeDisplayName"))){

                            playerKitManager.useKit(player, kit);
                            player.closeInventory();

                        } else if(inventoryClickEvent.getCurrentItem().getItemMeta()
                                .getDisplayName().equalsIgnoreCase("§cSchließen")){

                            kitManager.openKitInventory(player);
                        }
                    }
                }
            }
        }*/
    }
}
