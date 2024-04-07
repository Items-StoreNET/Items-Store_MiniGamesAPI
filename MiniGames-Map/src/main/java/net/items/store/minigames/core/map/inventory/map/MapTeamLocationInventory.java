package net.items.store.minigames.core.map.inventory.map;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.MapManager;
import net.items.store.minigames.core.map.location.LocationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MapTeamLocationInventory extends AbstractCustomInventory {

    public MapTeamLocationInventory() {
        super(MapInventoryIdentifier.MAP_IDENTIFIER_TEAM_LOCATION, "§eMap §8- §6{MAP_NAME}", 6*9);
    }

    @Override
    public void fillDefaultItems(Inventory inventory) {
        for(int i = 0; i < 9; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName("§0").buildItem());
        }
    }

    @Override
    public void fillCustomItems(Player player, Inventory inventory, Object... objects) {
        String teamName = getDataAsString(1, objects);
        ITeamManager teamManager = MiniGame.get(ITeamManager.class);
        ILocationManager locationManager = MiniGame.get(LocationManager.class);
        IMapManager mapManager = MiniGame.get(MapManager.class);

        GameTeam gameTeam = teamManager.getTeamFromName(teamName);
        if(gameTeam != null){
            inventory.setItem(5, gameTeam.getItemStack("§6Team " + gameTeam.getTeamName()));
        }
        inventory.setItem(3, ItemBuilder.modify().setMaterial(Material.GRASS_BLOCK)
                .setDisplayName("§6" + getDataAsString(0, objects)).buildItem());

        for (String key : mapManager.getLocationStateMap().keySet()) {
            LocationState locationState = mapManager.getLocationStateMap().get(key);
            if(locationState == LocationState.TEAM){
                Location location = locationManager.getLocation(key, locationState, getDataAsString(0, objects), teamName);
                ItemBuilder itemBuilder = ItemBuilder.modify().setMaterial(Material.PAPER)
                        .setDisplayName("§e" + key).setModifiedBuilder(location, key, "§7das Team §6" + teamName);
                inventory.addItem(itemBuilder.buildItem());
            }
        }
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        MapManager mapManager = MiniGame.get(MapManager.class);
        String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone()
                .getDisplayName().replace("§e", "");

        if (mapManager.getLocationStateKeysFromState(LocationState.TEAM).contains(displayName)) {
            ILocationManager locationManager = MiniGame.get(ILocationManager.class);
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            String mapName = player.getOpenInventory().getItem(3).getItemMeta().clone()
                    .getDisplayName().replace("§6", "");
            String dataName = player.getOpenInventory().getItem(5).getItemMeta().clone()
                    .getDisplayName().split(" ")[1];

            locationManager.setLocation(player, displayName, player.getLocation(), LocationState.TEAM, mapName, dataName);
        }
    }

    @Override
    public String getChangedInventoryName(Player player, Object... objects) {
        String changedInventoryName = inventoryName;
        changedInventoryName = changedInventoryName.replace("{MAP_NAME}", getDataAsString(0, objects));
        return changedInventoryName;
    }

    @Override
    public ItemStack getInventoryOpenItem(Player player) {
        return null;
    }

}
