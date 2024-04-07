package net.items.store.minigames.core.voting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.voting.*;
import net.items.store.minigames.core.data.MapBuilder;
import net.items.store.minigames.core.message.MessageManager;
import net.items.store.minigames.core.voting.listener.InventoryClickListener;
import net.items.store.minigames.core.voting.listener.PlayerInteractListener;
import net.items.store.minigames.core.voting.listener.PlayerJoinListener;
import net.items.store.minigames.core.voting.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VotingManager implements IVotingManager {

    protected final List<VotingHeader> votingHeaderList;
    private final List<VotingTrailer> votingWinnerList;

    public VotingManager(){
        this.votingHeaderList = Lists.newArrayList();
        this.votingWinnerList = Lists.newArrayList();
    }

    @Override
    public void addVoting(VotingHeader votingHeader) {
        this.votingHeaderList.add(votingHeader);
    }

    @Override
    public void openHeaderInventory(Player player, String votingIdentifier) {
        Inventory inventory = getVotingInventory(player, votingIdentifier);

        if(inventory == null) {
            VotingHeader votingHeader = getVotingHeader(votingIdentifier);

            if (votingHeader != null) {
                inventory = Bukkit.createInventory(null, votingHeader.getInventorySize(),
                        votingHeader.getInventoryName());

                for(VotingDetail votingDetail : votingHeader.getVotingDetailList()){
                    inventory.setItem(votingDetail.getInventorySlot(), votingDetail.getInventoryItem());
                }
            }
        }

        player.openInventory(inventory);
    }

    @Override
    public boolean isVotingInventory(String inventoryName, int inventorySize) {
        return this.votingHeaderList.stream().filter(x -> x.getInventoryName().equalsIgnoreCase(inventoryName)
                && x.getInventorySize() == inventorySize).count() > 0;
    }

    @Override
    public void openDetailInventory(Player player, String headerIdentifier, String detailIdentifier){
        Inventory inventory = null;
        VotingHeader votingHeader = getVotingHeader(headerIdentifier);
        if(votingHeader != null){
            inventory = Bukkit.createInventory(null, votingHeader.getInventorySize(),
                    votingHeader.getInventoryName());
        }

        if(inventory != null && votingHeader != null){
            VotingDetail votingDetail = getVotingDetail(headerIdentifier, detailIdentifier);

            if(votingDetail != null){
                for(VotingTrailer votingTrailer : votingDetail.getVotingTrailerList()){
                    inventory.setItem(votingTrailer.getItemSlot(), votingTrailer.getItem());
                }
            }
            player.openInventory(inventory);
        }
    }

    @Override
    public boolean clickInventory(Player player, String inventoryName, ItemStack clickedItem, int clickedSlot) {
        if(handleInventoryClick(player, inventoryName, clickedItem, clickedSlot) == true){
            return true;
        }

        VotingDetail votingDetail = getVotingDetail(inventoryName, clickedItem, clickedSlot);

        if(votingDetail != null){
            openDetailInventory(player, votingDetail.getVotingHeader().getVotingIdentifier(), votingDetail.getIdentifier());
            return true;
        }

        VotingTrailer votingTrailer = getVotingTrailer(inventoryName, clickedItem, clickedSlot);

        if(votingTrailer != null){
            vote(player, votingTrailer, player.getUniqueId());
            return true;
        }
        return false;
    }

    @Override
    public void endVoting() {
        this.votingWinnerList.clear();
        Map<Object, Object> objectObjectMap = Maps.newHashMap();
        IMapManager mapManager  = MiniGame.get(IMapManager.class);
        IMessageManager messageManager = MiniGame.get(MessageManager.class);

        for(VotingHeader votingHeader : votingHeaderList){
            for(VotingDetail votingDetail : votingHeader.getVotingDetailList()){
                VotingTrailer votingTrailer = votingDetail.getVotingWinner();

                this.votingWinnerList.add(votingTrailer);

                objectObjectMap.put("{DETAIL}", votingDetail.getIdentifier());
                objectObjectMap.put("{VOTING_NAME}", votingTrailer.getIdentifier());

                if(votingDetail.getIdentifier().equalsIgnoreCase("Map")
                    && mapManager != null){
                    GameMap gameMap = mapManager.getMapByName(votingTrailer.getIdentifier());

                    mapManager.setCurrentMap(gameMap);
                }

                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(messageManager.getMessage("VotingResult", objectObjectMap));
                }
            }
        }
    }

    @Override
    public void vote(Player player, VotingTrailer votingTrailer, UUID uuid) {
        VotingHeader votingHeader = votingTrailer.getVotingDetail().getVotingHeader();
        VotingDetail votingDetail = votingTrailer.getVotingDetail();
        IMessageManager messageManager = MiniGame.get(MessageManager.class);

        Map<Object, Object> objectObjectMap = MapBuilder.getObjectMap(
                new Object[] { "{DETAIL}", "{VOTING_NAME}" },
                new Object[]
                        {
                                votingHeader.getVotingIdentifier().toLowerCase().contains("map")
                                        ? "die Map" : votingDetail.getNameWithPrefix(),
                                votingTrailer.getIdentifier()
                        });

        if(votingDetail.playerAlreadyInVoting(votingTrailer.getIdentifier(), uuid)){
            player.sendMessage(messageManager.getMessage("AlreadyVoted", objectObjectMap));
            return;
        }

        votingDetail.addPlayerToVoting(votingTrailer, uuid);
        player.sendMessage(messageManager.getMessage("Voted", objectObjectMap));
    }

    @Override
    public List<VotingDetail> getVotingDetails(String votingIdentifier) {
        VotingHeader votingHeader = getVotingHeader(votingIdentifier);

        return votingHeader != null ? votingHeader.getVotingDetailList() : Lists.newArrayList();
    }

    @Override
    public List<VotingTrailer> getVotingWinners() {
        return this.votingWinnerList;
    }

    @Override
    public VotingDetail getVotingDetail(String headerIdentifier, String detailIdentifier) {
        VotingHeader votingHeader = getVotingHeader(headerIdentifier);
        VotingDetail votingDetail = votingHeader.getVotingDetail(detailIdentifier);

        return votingDetail;
    }

    @Override
    public VotingHeader getVotingHeader(String votingIdentifier) {
        return votingHeaderList.stream()
                .filter(x -> x.getVotingIdentifier().equalsIgnoreCase(votingIdentifier)).findAny().orElse(null);
    }

    @Override
    public List<VotingHeader> getVotingHeaders() {
        return this.votingHeaderList;
    }

    @Override
    public boolean handleInventoryClick(Player player, String inventoryName, ItemStack clickedItem, int clickedSlot) {
        return false;
    }

    @Override
    public Inventory getVotingInventory(Player player, String votingIdentifier) {
        return null;
    }

    protected VotingTrailer getVotingTrailer(String inventoryName, ItemStack clickedItem, int clickedSlot){
        VotingHeader votingHeader = votingHeaderList.stream()
                .filter(x -> x.getInventoryName().equalsIgnoreCase(inventoryName)
                        && x.getVotingDetail(clickedItem, clickedSlot) != null)
                .findAny().orElse(null);

        if(votingHeader != null){
            return votingHeader.getVotingTrailer(clickedItem, clickedSlot);
        }
        return null;
    }

    private VotingDetail getVotingDetail(String inventoryName, ItemStack clickedItem, int clickedSlot){
        VotingHeader votingHeader = votingHeaderList.stream()
                .filter(x -> x.getInventoryName().equalsIgnoreCase(inventoryName)
                        && x.getVotingDetail(clickedItem, clickedSlot) != null)
                .findAny().orElse(null);

        if(votingHeader != null){
            return votingHeader.getVotingDetail(clickedItem, clickedSlot);
        }
        return null;
    }

    @Override
    public void registerDefault() {
        PluginManager pluginManager = MiniGame.getJavaPlugin().getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), MiniGame.getJavaPlugin());
        pluginManager.registerEvents(new PlayerInteractListener(), MiniGame.getJavaPlugin());
        pluginManager.registerEvents(new PlayerJoinListener(), MiniGame.getJavaPlugin());
        pluginManager.registerEvents(new PlayerQuitListener(), MiniGame.getJavaPlugin());

        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("AlreadyVoted", "{PREFIX}§cDu hast bereits für {DETAIL} §a{VOTING_NAME} §cgevotet!");
        messageManager.addMessage("Voted", "{PREFIX}§cDu hast erfolgreich für {DETAIL} §a{VOTING_NAME} §cgevotet!");
        messageManager.addMessage("VotingResult", "{PREFIX}§7{DETAIL}§8: §a{VOTING_NAME}");
    }
}
