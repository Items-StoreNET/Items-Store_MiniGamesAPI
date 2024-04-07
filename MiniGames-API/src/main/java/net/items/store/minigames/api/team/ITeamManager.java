package net.items.store.minigames.api.team;

import net.items.store.minigames.api.IDefaultManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ITeamManager extends IDefaultManager {

    /**
     * Adds a Team to the Team List
     * @param team
     */
    void addTeam(GameTeam team);

    /**
     * Change the Team of a Player
     * @param player
     * @param teamName
     */
    void joinTeam(Player player, String teamName);

    boolean showActionBar();

    /**
     * Removes a Player from his current Team
     * @param player
     */
    void leaveTeam(Player player);

    /**
     * Adds a Player to a Random Team when Player not already in a Team
     * @param player
     * @return if player got a random Team or not
     */
    boolean randomTeamForPlayer(Player player);

    GameTeam getTeamFromItemStack(ItemStack itemStack);

    void setItemToInventory(Player player);

    boolean compareItems(ItemStack itemStack);

    void updateKitInventoryData(String inventoryName, int inventorySize, ItemStack kitInventoryItem, int kitInventorySlot, boolean kitInventoryActive);

    void openTeamInventory(Player player);

    String getInventoryName();

    /**
     * @param player
     * @return the Team from the Player
     */
    GameTeam getPlayerTeam(Player player);

    GameTeam getTeamFromName(String teamName);

    /**
     * @return the complete Team List
     */
    List<GameTeam> getTeams();

    /**
     * Updates the Prefix from a Player to the Prefix of his current Team
     * @param player
     */
    void updatePlayerTeamPrefix(Player player);

}
