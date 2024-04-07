package net.items.store.minigames.api.item;

import org.bukkit.inventory.ItemStack;

public class ItemHelper {

    public static boolean compareItems(ItemStack firstItemStack, ItemStack secondItemStack){
        return firstItemStack != null && secondItemStack != null
                && firstItemStack.getType().equals(secondItemStack.getType())
                && ((firstItemStack.getItemMeta() != null && secondItemStack.getItemMeta() != null)
                && firstItemStack.getItemMeta().getDisplayName().equalsIgnoreCase(secondItemStack
                .getItemMeta().getDisplayName()));
    }
}
