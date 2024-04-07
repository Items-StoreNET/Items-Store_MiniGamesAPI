package net.items.store.minigames.core.kit.inventory;

import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.IPlayerKitManager;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.kit.KitInventoryIdentifier;
import net.items.store.minigames.core.kit.KitManager;
import net.items.store.minigames.core.kit.PlayerKitManager;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class KitBuyInventory extends AbstractCustomInventory {

    public KitBuyInventory(){
        super(KitInventoryIdentifier.KIT_IDENTIFIER_KIT_BUY, "{KIT_NAME}", 27);
    }

    @Override
    public void fillDefaultItems(Inventory inventory) {
        for(int i = 0; i < 8; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName("§0").buildItem());
        }
        for(int i = 18; i < 27; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName("§0").buildItem());
        }
    }

    @Override
    public void fillCustomItems(Player player, Inventory inventory, Object... objects) {
        Kit kit = null;

        if (objects.length > 0){
            kit = (Kit) objects[0];
        }

        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        Map<Object, Object> objectObjectMap = Maps.newHashMap();
        objectObjectMap.put("{COINS}", kit.getKitPrice());

        inventory.setItem(8, ItemBuilder.modify().setMaterial(Material.RED_STAINED_GLASS_PANE)
                .setDisplayName(messageManager.getMessage("KitBuyClose")).buildItem());

        inventory.setItem(13, kit.getKitMainItem().buildItem());
        inventory.setItem(17, ItemBuilder.modify().setMaterial(Material.GOLD_INGOT)
                .setDisplayName(messageManager.getMessage("KitBuyDisplayName"))
                .setLore(Arrays.stream(messageManager.getMessage("KitBuyLore", objectObjectMap).split("%NL%")).toList())
                .buildItem());
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player) inventoryClickEvent.getWhoClicked();
        IKitManager kitManager = MiniGame.get(KitManager.class);
        Optional<Kit> optionalKit = kitManager.getKits().stream()
                .filter(x -> player.getOpenInventory().getTitle().contains(x.getKitName())).findFirst();

        if(optionalKit.isPresent()){
            inventoryClickEvent.setCancelled(true);
            Kit kit = optionalKit.get();
            IPlayerKitManager playerKitManager = MiniGame.get(PlayerKitManager.class);
            IMessageManager messageManager = MiniGame.get(MessageManager.class);

            if(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(messageManager.getMessage("KitBuyDisplayName"))){
                playerKitManager.buyKit(player, kit);
                player.closeInventory();
            } else if(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cSchließen")){
                IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
                inventoryManager.openInventory(player, KitInventoryIdentifier.KIT_IDENTIFIER_DEFAULT);
            }
        }
    }

    @Override
    public String getChangedInventoryName(Player player, Object... objects) {
        String kitName = "";

        if (objects.length > 0){
            Kit kit = (Kit) objects[0];
            kitName = kit.getKitName();
        }

        String changedInventoryName = inventoryName;
        changedInventoryName = changedInventoryName.replace("{KIT_NAME}", kitName);
        return changedInventoryName;
    }

    @Override
    public ItemStack getInventoryOpenItem(Player player) {
        return null;
    }
}
