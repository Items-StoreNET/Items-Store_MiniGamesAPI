package net.items.store.minigames.core.kit.inventory;

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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultKitInventory extends AbstractCustomInventory {

    public DefaultKitInventory(ItemStack inventoryOpenItem, String inventoryName, int inventorySize){
        super(KitInventoryIdentifier.KIT_IDENTIFIER_DEFAULT, inventoryName, inventorySize);

        this.inventoryOpenItem = inventoryOpenItem;
    }

    @Override
    public void fillDefaultItems(Inventory inventory) {

    }

    @Override
    public void fillCustomItems(Player player, Inventory inventory, Object... objects) {
        IKitManager kitManager = MiniGame.get(KitManager.class);
        IPlayerKitManager playerKitManager = MiniGame.get(IPlayerKitManager.class);
        IMessageManager messageManager = MiniGame.get(MessageManager.class);

        for (Kit kit : kitManager.getKits()) {
            ItemBuilder itemBuilder = kit.getKitMainItem().copy();

            if (playerKitManager.hasKit(player, kit)) {
                if (playerKitManager.getPlayerKit(player).getKitName().equalsIgnoreCase(kit.getKitName())) {
                    itemBuilder.setDisplayName(itemBuilder.getDisplayName() +
                            messageManager.getMessage("KitActive"));
                } else {
                    itemBuilder.setDisplayName(itemBuilder.getDisplayName() +
                            messageManager.getMessage("KitBuyed"));
                }
            } else {
                itemBuilder.setDisplayName(itemBuilder.getDisplayName() +
                        messageManager.getMessage("KitNotBuyed"));
            }
            inventory.addItem(itemBuilder.buildItem());
        }
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player) inventoryClickEvent.getWhoClicked();
        IKitManager kitManager = MiniGame.get(KitManager.class);
        IPlayerKitManager playerKitManager = MiniGame.get(PlayerKitManager.class);
        Kit kit = kitManager.getKitFromItemStack(inventoryClickEvent.getCurrentItem());

        if(kit != null){
            IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);

            if(playerKitManager.hasKit(player, kit)){
                inventoryManager.openInventory(player, KitInventoryIdentifier.KIT_IDENTIFIER_KIT_USE, kit);
            } else {
                inventoryManager.openInventory(player, KitInventoryIdentifier.KIT_IDENTIFIER_KIT_BUY, kit);
            }
        }
    }

    @Override
    public String getChangedInventoryName(Player player, Object... objects) {
        return inventoryName;
    }

    @Override
    public ItemStack getInventoryOpenItem(Player player) {
        return inventoryOpenItem;
    }

    private ItemStack inventoryOpenItem;
}
