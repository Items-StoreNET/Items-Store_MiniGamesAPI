package net.items.store.minigames.api.kit;

import net.items.store.minigames.api.IDefaultManager;
import org.bukkit.entity.Player;

import java.util.List;

public interface IPlayerKitManager extends IDefaultManager {

    void createUser(Player player);

    void useKit(Player player, Kit kit);

    boolean hasKit(Player player, Kit kit);

    boolean buyKit(Player player, Kit kit);

    /**
     * @param player
     * @return player kit, if it exists, otherwise it give null back.
     */
    PlayerKit getPlayerKit(Player player);

    List<PlayerKit> getPlayerKits(Player player);

    /**
     * Get all kits from the player and gives him one random.
     * @param player
     */
    void takeRandomKit(Player player);

}
