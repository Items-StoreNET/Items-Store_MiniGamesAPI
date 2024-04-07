package net.items.store.minigames.api.team;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class GameTeam {
	
	private String teamName;
	private String teamPrefix;
	private String teamFullPrefix;
	private String scoreboardTeamName;
	private int maxPlayers;
	private List<UUID> teamPlayers;
	@Setter
	private boolean teamBedExists;
	@Setter
	private ItemStack teamInventoryItem;
	@Setter
	private Color teamColor;
	private int teamInventorySlot;
	private ItemStack teamItem;
	
	public GameTeam(String teamName, String teamPrefix, String teamFullPrefix, String scoreboardTeamName,
					int maxPlayers, int teamInventorySlot, ItemStack teamItem) {
		this.teamName = teamName;
		this.teamPrefix = teamPrefix;
		this.teamFullPrefix = teamFullPrefix;
		this.scoreboardTeamName = scoreboardTeamName;
		this.maxPlayers = maxPlayers;
		this.teamPlayers = new ArrayList<>();
		this.teamBedExists = false;
		this.teamInventoryItem = null;
		this.teamColor = null;
		this.teamInventorySlot = teamInventorySlot;
		this.teamItem = teamItem;
	}

	public ItemStack getItemStack(String displayName){
		ItemStack itemStack = teamItem.clone();
		ItemMeta itemMeta = itemStack.getItemMeta().clone();
		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(Lists.newArrayList());
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public ItemStack getTeamItem(){
		ItemStack itemStack = teamItem.clone();
		ItemMeta itemMeta = itemStack.getItemMeta().clone();
		List<String> oldLoreList = itemMeta.getLore();
		List<String> loreList = Lists.newArrayList();

		String displayName = itemMeta.getDisplayName();
		displayName = displayName.replace("{TEAMPREFIX}", this.teamPrefix)
				.replace("{TEAMNAME}", this.teamName)
				.replace("{TEAMSIZE}", "" + this.teamPlayers.size())
				.replace("{MAXTEAMSIZE}","" + this.maxPlayers);

		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(Lists.newArrayList());

		for(String string : oldLoreList){
			if(string.contains("{PLAYERNAME}")){
				for(UUID uuid : this.teamPlayers){
					loreList.add(string.replace("{TEAMPREFIX}", this.teamPrefix)
							.replace("{PLAYERNAME}", Bukkit.getPlayer(uuid).getName()));
				}
				for(int i = this.teamPlayers.size(); i < this.maxPlayers; i++){
					loreList.add(string.replace("{TEAMPREFIX}", "")
							.replace("{PLAYERNAME}", "§8§m-"));
				}
			} else {
				loreList.add(string.replace("{TEAMPREFIX}", this.teamPrefix)
						.replace("{TEAMNAME}", this.teamName));
			}
		}
		itemMeta.setLore(loreList);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
}