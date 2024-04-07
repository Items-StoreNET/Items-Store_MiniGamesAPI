package net.items.store.minigames.api.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractCustomInventory {

    private final List<Integer> DEFAULT_SLOTS = Lists.newArrayList();
    protected String identifier;
    protected String inventoryName;
    protected int inventorySize;

    public AbstractCustomInventory(String identifier, String inventoryName, int inventorySize){
        this.identifier = identifier;
        this.inventoryName = inventoryName;
        this.inventorySize = inventorySize;
    }

    public void onInventoryClick(InventoryClickEvent inventoryClickEvent){
        Inventory clickedInventory = inventoryClickEvent.getClickedInventory();

        if (clickedInventory != null){
            ItemStack currentItem = inventoryClickEvent.getCurrentItem();

            if (currentItem != null && currentItem.getItemMeta() != null && currentItem.getItemMeta().getDisplayName() != null) {
                inventoryClickEvent.setCancelled(true);

                performInventoryClick(inventoryClickEvent);
            }
        }
    }

    public boolean onPlayerInteract(PlayerInteractEvent playerInteractEvent){
        boolean openInventory = false;
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = getInventoryOpenItem(player);

        if (itemStack != null) {
            Action action = playerInteractEvent.getAction();

            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = playerInteractEvent.getItem();

                if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null
                        && (item.equals(itemStack) || itemEquals(player, item))) {
                    openInventory = true;
                }
            }
        }

        return openInventory;
    }

    public Inventory openInventory(Player player, Object... objects){
        String changedInventoryName = getChangedInventoryName(player, objects);
        Inventory inventory = Bukkit.createInventory(null, inventorySize, changedInventoryName);

        fillDefaultItems(inventory);

        for (int i = 0; i < inventorySize; i++){
            if (inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR){
                DEFAULT_SLOTS.add(i);
            }
        }

        fillCustomItems(player, inventory, objects);

        player.openInventory(inventory);
        return inventory;
    }

    public void updateInventory(Inventory inventory, Player player, Object... objects){
        for (int i = 0; i < inventorySize; i++){
            if (DEFAULT_SLOTS.contains(i) == false){
                inventory.clear(i);
            }
        }

        fillCustomItems(player, inventory, objects);
    }

    public String getDataAsString(int count, Object... objects){
        if (objects.length > count){
            return objects[count].toString();
        }

        return "";
    }

    public int getDataAsInt(int count, Object... objects){
        if (objects.length > count){
            return (int) objects[count];
        }

        return 0;
    }

    public boolean itemEquals(Player player, ItemStack targetItem){
        ItemStack itemStack = getInventoryOpenItem(player);

        if (targetItem.getItemMeta().getDisplayName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName()) == false){
            return false;
        }
        if (targetItem.getType().equals(itemStack.getType()) == false){
            return false;
        }

        // TODO: Add Lore and Enchantments for Equals
        return true;
    }

    public abstract void fillDefaultItems(Inventory inventory);

    public abstract void fillCustomItems(Player player, Inventory inventory, Object... objects);

    public abstract void performInventoryClick(InventoryClickEvent inventoryClickEvent);

    public abstract String getChangedInventoryName(Player player, Object... objects);

    public abstract ItemStack getInventoryOpenItem(Player player);

}
