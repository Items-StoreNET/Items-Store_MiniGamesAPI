package net.items.store.minigames.core.voting;

import net.items.store.minigames.api.voting.VotingDetail;
import net.items.store.minigames.api.voting.VotingHeader;
import net.items.store.minigames.api.voting.VotingTrailer;
import net.items.store.minigames.core.data.FileBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.Optional;

public class MapVotingManager extends VotingManager {

    private String mapInventoryName;

    public MapVotingManager() {
        super();

        JSONObject jsonObject = FileBuilder.loadJSONObject("mapVoting.json", true);
        JSONObject inventoryJsonObject = (JSONObject) jsonObject.get("mapVotingInventory");

        mapInventoryName = inventoryJsonObject.get("inventoryName").toString();
    }

    @Override
    public boolean handleInventoryClick(Player player, String inventoryName, ItemStack clickedItem, int clickedSlot) {
        VotingTrailer votingTrailer = getVotingTrailer(inventoryName, clickedItem, clickedSlot);

        if(votingTrailer != null){
            super.vote(player, votingTrailer, player.getUniqueId());
            return true;
        }
        return false;
    }

    @Override
    public Inventory getVotingInventory(Player player, String votingIdentifier) {
        Inventory inventory = null;
        VotingHeader votingHeader = getVotingHeader(votingIdentifier);
        int[] inventorySlots = new int[] { 11, 13, 15 };

        if (votingHeader != null && votingHeader.getVotingDetailList().size() > 0) {
            inventory = Bukkit.createInventory(null, votingHeader.getInventorySize(),
                    votingHeader.getInventoryName());

            VotingDetail votingDetail = votingHeader.getVotingDetailList().get(0);

            for (int i = 0; i < 3; i++){
                if(votingDetail.getVotingTrailerList().size() > i){
                    VotingTrailer votingTrailer = votingDetail.getVotingTrailerList().get(i);

                    inventory.setItem(inventorySlots[i], votingTrailer.getItem());
                }
            }
        }

        return inventory;
    }

    @Override
    protected VotingTrailer getVotingTrailer(String inventoryName, ItemStack clickedItem, int clickedSlot){
        if (inventoryName.contains(mapInventoryName) == true) {
            VotingHeader votingHeader = this.votingHeaderList.stream()
                    .filter(x -> x.getInventoryName().equalsIgnoreCase(inventoryName)
                            && x.getVotingTrailer(clickedItem) != null)
                    .findAny().orElse(null);

            if(votingHeader != null){
                return votingHeader.getVotingTrailer(clickedItem);
            }
        } else {
            return super.getVotingTrailer(inventoryName, clickedItem, clickedSlot);
        }
        return null;
    }
}
