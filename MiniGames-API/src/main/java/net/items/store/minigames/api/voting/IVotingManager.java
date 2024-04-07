package net.items.store.minigames.api.voting;

import net.items.store.minigames.api.IDefaultManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface IVotingManager extends IDefaultManager {

    /**
     * Adding new VotingDetail to the VotingDetail-List
     * @param votingHeader
     */
    void addVoting(VotingHeader votingHeader);

    void openHeaderInventory(Player player, String votingIdentifier);

    void openDetailInventory(Player player, String headerIdentifier, String detailIdentifier);

    boolean isVotingInventory(String inventoryName, int inventorySize);

    boolean clickInventory(Player player, String inventoryName, ItemStack clickedItem, int clickedSlot);

    /**
     * Ends the VotingDetail and display the result
     */
    void endVoting();

    void vote(Player player, VotingTrailer votingTrailer, UUID uuid);

    /**
     * @return the VotingDetail-List
     */
    List<VotingDetail> getVotingDetails(String votingIdentifier);

    List<VotingTrailer> getVotingWinners();

    VotingDetail getVotingDetail(String headerIdentifier, String detailIdentifier);

    /**
     * @return the VotingDetail-List
     */
    VotingHeader getVotingHeader(String votingIdentifier);

    List<VotingHeader> getVotingHeaders();

    boolean handleInventoryClick(Player player, String inventoryName, ItemStack clickedItem, int clickedSlot);

    Inventory getVotingInventory(Player player, String votingIdentifier);

}
