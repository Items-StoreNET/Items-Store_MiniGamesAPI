package net.items.store.minigames.api.inventory;

import org.bukkit.entity.Player;

public interface IInventoryManager {

    void addCustomInventory(AbstractCustomInventory abstractCustomInventory);

    boolean openInventory(Player player, String identifier, Object... objects);

    AbstractCustomInventory findInventory(String identifier);

}