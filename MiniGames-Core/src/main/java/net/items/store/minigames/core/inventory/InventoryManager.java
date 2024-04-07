package net.items.store.minigames.core.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kotlin.Pair;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.inventory.IInventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryManager implements IInventoryManager, Listener {

    private List<AbstractCustomInventory> abstractCustomInventoryList;
    private static final Map<UUID, Pair<Inventory, String>> UUID_INVENTORY_MAP = Maps.newHashMap();

    public InventoryManager(){
        this.abstractCustomInventoryList = Lists.newArrayList();
    }

    @Override
    public void addCustomInventory(AbstractCustomInventory abstractCustomInventory) {
        this.abstractCustomInventoryList.add(abstractCustomInventory);
    }

    @Override
    public boolean openInventory(Player player, String identifier, Object... objects) {
        AbstractCustomInventory abstractCustomInventory = findInventory(identifier);

        if (abstractCustomInventory == null){
            return false;
        }

        Inventory inventory = abstractCustomInventory.openInventory(player, objects);

        UUID_INVENTORY_MAP.put(player.getUniqueId(), new Pair<>(inventory, identifier));
        return true;
    }

    @Override
    public AbstractCustomInventory findInventory(String identifier) {
        return this.abstractCustomInventoryList
                .stream()
                .filter(x -> x.getIdentifier().equalsIgnoreCase(identifier))
                .findAny()
                .orElse(null);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent){
        UUID uuid = inventoryClickEvent.getWhoClicked().getUniqueId();

        if (UUID_INVENTORY_MAP.containsKey(uuid)){
            String identifier = UUID_INVENTORY_MAP.get(uuid).getSecond();
            AbstractCustomInventory abstractCustomInventory = findInventory(identifier);
            abstractCustomInventory.onInventoryClick(inventoryClickEvent);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent){
        for (AbstractCustomInventory abstractCustomInventory : this.abstractCustomInventoryList){
            if (abstractCustomInventory.onPlayerInteract(playerInteractEvent)){
                openInventory(playerInteractEvent.getPlayer(), abstractCustomInventory.getIdentifier());
                break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent){
        if (UUID_INVENTORY_MAP.containsKey(inventoryCloseEvent.getPlayer().getUniqueId())){
            UUID_INVENTORY_MAP.remove(inventoryCloseEvent.getPlayer().getUniqueId());
        }
    }
}
