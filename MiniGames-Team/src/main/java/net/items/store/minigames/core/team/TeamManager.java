package net.items.store.minigames.core.team;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.data.DataBuilder;
import net.items.store.minigames.core.data.FileBuilder;
import net.items.store.minigames.core.message.MessageManager;
import net.items.store.minigames.core.team.listener.InventoryClickListener;
import net.items.store.minigames.core.team.listener.PlayerInteractListener;
import net.items.store.minigames.core.team.listener.PlayerJoinListener;
import net.items.store.minigames.core.team.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class TeamManager implements ITeamManager {

    private List<GameTeam> abstractTeamList;
    private String inventoryName;
    private int inventorySize;
    private ItemStack kitInventoryItem;
    private int kitInventorySlot;
    private boolean kitInventoryActive;

    public TeamManager(){
        this.abstractTeamList = Lists.newArrayList();

        loadTeams();
    }

    @Override
    public void addTeam(GameTeam team) {
        this.abstractTeamList.add(team);
    }

    @Override
    public void joinTeam(Player player, String teamName) {
        GameTeam team = null;

        for(GameTeam targetTeam : abstractTeamList){
            if(targetTeam.getTeamName().equalsIgnoreCase(teamName)){
                team = targetTeam;
                break;
            }
        }
        if(team == null){
            player.sendMessage("§4Error: §cPlease contact an Admin.");
            return;
        }
        Map<Object, Object> objectObjectMap = Maps.newHashMap();
        objectObjectMap.put("{TEAMNAME}", team.getTeamName());
        objectObjectMap.put("{TEAMPREFIX}", team.getTeamPrefix());

        IMessageManager messageManager = MiniGame.get(MessageManager.class);

        if(team.getTeamPlayers().contains(player.getUniqueId())){
            player.sendMessage(messageManager.getMessage("AlreadyInTeam", objectObjectMap));
            return;
        }
        if(team.getTeamPlayers().size() >= team.getMaxPlayers()){
            player.sendMessage(messageManager.getMessage("TeamIsFull", objectObjectMap));
            return;
        }
        GameTeam currentPlayerTeam = getPlayerTeam(player);
        if(currentPlayerTeam != null){
            currentPlayerTeam.getTeamPlayers().remove(player.getUniqueId());
        }

        team.getTeamPlayers().add(player.getUniqueId());
        player.sendMessage(messageManager.getMessage("JoinedTeam", objectObjectMap));
    }

    @Override
    public void leaveTeam(Player player) {
        GameTeam abstractTeam = getPlayerTeam(player);
        if(abstractTeam != null){
            abstractTeam.getTeamPlayers().remove(player.getUniqueId());
        }
    }

    @Override
    public boolean randomTeamForPlayer(Player player) {
        GameTeam abstractTeam = getPlayerTeam(player);
        if(abstractTeam == null) {
            GameTeam team = null;

            for(GameTeam targetTeam : abstractTeamList){
                if(targetTeam.getTeamPlayers().size() < targetTeam.getMaxPlayers()){
                    team = targetTeam;
                    break;
                }
            }
            Map<Object, Object> objectObjectMap = Maps.newHashMap();
            objectObjectMap.put("{TEAMNAME}", team.getTeamName());
            objectObjectMap.put("{TEAMPREFIX}", team.getTeamPrefix());

            IMessageManager messageManager = MiniGame.get(MessageManager.class);

            team.getTeamPlayers().add(player.getUniqueId());
            player.sendMessage(messageManager.getMessage("JoinedTeam", objectObjectMap));
            return true;
        }
        return false;
    }

    @Override
    public GameTeam getTeamFromItemStack(ItemStack itemStack) {
        GameTeam gameTeam = null;

        for(GameTeam currentGameTeam : abstractTeamList){
            ItemStack mainItem = currentGameTeam.getTeamItem();

            if(itemStack.getItemMeta().getDisplayName().contains(mainItem.getItemMeta().getDisplayName()) &&
                    mainItem.getType().name().equalsIgnoreCase(itemStack.getType().name())){
                gameTeam = currentGameTeam;
                break;
            }
        }
        return gameTeam;
    }

    @Override
    public void setItemToInventory(Player player) {
        if(this.kitInventoryActive){
            if(this.kitInventoryItem != null){
                player.getInventory().setItem(this.kitInventorySlot, this.kitInventoryItem);
            }
        }
    }

    @Override
    public boolean compareItems(ItemStack itemStack) {
        if(this.kitInventoryItem != null){
            if(itemStack.getType() == this.kitInventoryItem.getType()
                    && itemStack.getItemMeta().getDisplayName()
                    .equalsIgnoreCase(this.kitInventoryItem.getItemMeta().getDisplayName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateKitInventoryData(String inventoryName, int inventorySize, ItemStack kitInventoryItem, int kitInventorySlot, boolean kitInventoryActive) {
        this.inventoryName = inventoryName;
        this.inventorySize = inventorySize;
        this.kitInventoryItem = kitInventoryItem;
        this.kitInventorySlot = kitInventorySlot;
        this.kitInventoryActive = kitInventoryActive;
    }

    @Override
    public void openTeamInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, this.inventorySize, this.inventoryName);

        for(GameTeam gameTeam : abstractTeamList){
            inventory.setItem(gameTeam.getTeamInventorySlot(), gameTeam.getTeamItem());
        }

        player.openInventory(inventory);
    }

    @Override
    public String getInventoryName() {
        return this.inventoryName;
    }

    @Override
    public GameTeam getPlayerTeam(Player player) {
        GameTeam team = null;

        for(GameTeam targetTeam : abstractTeamList){
            if(targetTeam.getTeamPlayers().contains(player.getUniqueId())){
                team = targetTeam;
                break;
            }
        }
        return team;
    }

    @Override
    public boolean showActionBar() {
        return true;
    }

    @Override
    public GameTeam getTeamFromName(String teamName) {
        GameTeam team = null;

        for(GameTeam targetTeam : abstractTeamList){
            if(targetTeam.getTeamName().equalsIgnoreCase(teamName)){
                team = targetTeam;
                break;
            }
        }
        return team;
    }

    @Override
    public List<GameTeam> getTeams() {
        return abstractTeamList;
    }

    @Override
    public void updatePlayerTeamPrefix(Player player) {

    }

    @Override
    public void registerDefault() {
        PluginManager pluginManager = MiniGame.getJavaPlugin().getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), MiniGame.getJavaPlugin());
        pluginManager.registerEvents(new PlayerInteractListener(), MiniGame.getJavaPlugin());
        pluginManager.registerEvents(new InventoryClickListener(), MiniGame.getJavaPlugin());
        pluginManager.registerEvents(new PlayerQuitListener(), MiniGame.getJavaPlugin());

        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("AlreadyInTeam", "{PREFIX}§cDu bist bereits im {TEAMPREFIX}Team {TEAMNAME}§c.");
        messageManager.addMessage("TeamIsFull", "{PREFIX}§cDas {TEAMPREFIX}Team {TEAMNAME} §cist bereits voll.");
        messageManager.addMessage("JoinedTeam", "{PREFIX}§7Du bist nun im {TEAMPREFIX}Team {TEAMNAME}§7.");
    }

    private void loadTeams(){
        JSONObject jsonObject = FileBuilder.loadJSONObject("teams.json", true);
        int inventorySlot = Integer.valueOf(jsonObject.get("inventorySlot").toString());
        ItemStack inventoryItem = DataBuilder
                .getItemBuilderFromJSONObject((JSONObject) jsonObject.get("inventoryItem")).buildItem();
        boolean inventoryItemActive = Boolean.valueOf(jsonObject.get("inventoryItemActive").toString());
        String inventoryName = jsonObject.get("inventoryName").toString();
        int inventorySize = Integer.valueOf(jsonObject.get("inventorySize").toString());

        updateKitInventoryData(inventoryName, inventorySize, inventoryItem, inventorySlot, inventoryItemActive);

        JSONArray jsonArray = (JSONArray) jsonObject.get("teams");
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject teamJSONObject = (JSONObject) jsonArray.get(i);

            String teamName = teamJSONObject.get("teamName").toString();
            String teamPrefix = teamJSONObject.get("teamPrefix").toString();
            String fullTeamPrefix = teamJSONObject.get("teamFullPrefix").toString();
            String scoreboardTeamName = teamJSONObject.get("teamScoreboardName").toString();
            int maxPlayers = teamJSONObject.containsKey("teamSize") ? Integer.valueOf(teamJSONObject.get("teamSize").toString()) : 0;
            int teamInventorySlot = teamJSONObject.containsKey("teamInventorySlot") ? Integer.valueOf(teamJSONObject.get("teamInventorySlot").toString()) : 0;

            ItemStack teamItem = null;
            if (teamJSONObject.containsKey("teamItem")){
                teamItem = DataBuilder.getItemBuilderFromJSONObject((JSONObject) teamJSONObject.get("teamItem")).buildItem();
            }

            GameTeam gameTeam = new GameTeam(teamName, teamPrefix, fullTeamPrefix, scoreboardTeamName, maxPlayers, teamInventorySlot, teamItem);
            addTeam(gameTeam);
        }
    }
}
