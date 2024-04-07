package net.items.store.minigames.api.kit;

import net.items.store.minigames.api.IDefaultManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IKitManager extends IDefaultManager {

	/**
	 * Adds a kit to the kit-List
	 * @param kit
	 */
	void addKit(Kit kit);

	/**
	 * @return the kit-List
	 */
	List<Kit> getKits();

	Kit getKitFromItemStack(ItemStack itemStack);

	Kit getKitFromName(String kitName);

	Kit getDefaultKit();

	void updateKitInventoryData(ItemStack kitInventoryItem, int kitInventorySlot, boolean kitInventoryActive);

	boolean compareItems(ItemStack itemStack);

	void setItemToInventory(Player player);

	/**
	 * Clear the players inventory and give the given player the abstractKit.
	 * @param player
	 * @param kit
	 */
	void givePlayerKit(Player player, Kit kit);

	String getInventoryName();

}
