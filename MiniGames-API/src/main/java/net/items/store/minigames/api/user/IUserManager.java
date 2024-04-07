package net.items.store.minigames.api.user;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IUserManager {

	/**
	 * Loads user instance with the uniqueID
	 * @param uniqueID of the searching User
	 * @return User instance
	 */
	User loadUser(UUID uniqueID);
	
	/**
	 * Loads the Lobby Inventory of the Player
	 * @param player
	 */
	void loadLobbyInventory(Player player);
	
}
