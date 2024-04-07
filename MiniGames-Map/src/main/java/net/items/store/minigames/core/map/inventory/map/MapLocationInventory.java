package net.items.store.minigames.core.map.inventory.map;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.MapManager;
import net.items.store.minigames.core.map.location.LocationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MapLocationInventory extends AbstractCustomInventory {

    public MapLocationInventory() {
        super(MapInventoryIdentifier.MAP_IDENTIFIER_LOCATION, "§eMap §8- §6{MAP_NAME}", 6*9);
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
        ILocationManager locationManager = MiniGame.get(LocationManager.class);
        ITeamManager teamManager = MiniGame.get(ITeamManager.class);
        IMapManager mapManager = MiniGame.get(MapManager.class);

        inventory.setItem(4, ItemBuilder.modify().setMaterial(Material.GRASS_BLOCK)
                .setDisplayName("§6" + getDataAsString(0, objects)).buildItem());
        inventory.addItem(ItemBuilder.modify().setMaterial(Material.PAPER)
                .setDisplayName("§eBlock ändern").buildItem());

        for (String key : mapManager.getLocationStateMap().keySet()){
            LocationState locationState = mapManager.getLocationStateMap().get(key);

            switch (locationState){
                case COUNT:
                    inventory.addItem(ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§eCount " + key).buildItem());
                    break;
                case NORMAL:
                    Location location = locationManager.getLocation(key, locationState, getDataAsString(0, objects));
                    ItemBuilder itemBuilder = ItemBuilder.modify().setMaterial(Material.PAPER)
                            .setDisplayName("§e" + key).setModifiedBuilder(location, key);
                    inventory.addItem(itemBuilder.buildItem());
                    break;
                default:
                    break;
            }
        }
        if(mapManager.getLocationStateMap().values().contains(LocationState.TEAM)){
            for(GameTeam gameTeam : teamManager.getTeams()){
                inventory.addItem(ItemBuilder.modify().setMaterial(Material.PAPER)
                        .setDisplayName("§eTeam " + gameTeam.getTeamName()).buildItem());
            }
        }
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        ILocationManager locationManager = MiniGame.get(ILocationManager.class);

        Player player = (Player) inventoryClickEvent.getWhoClicked();
        String mapName = player.getOpenInventory().getItem(4).getItemMeta().clone()
                .getDisplayName().replace("§6", "");
        String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone()
                .getDisplayName().replace("§e", "");
        IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);

        if (displayName.contains("Count")) {
            inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_COUNT, mapName, displayName.split(" ")[1]);
        } else if (displayName.contains("Team")) {
            inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_TEAM_LOCATION, mapName, displayName.split(" ")[1]);
        } else if (displayName.equals("Block ändern")) {
            inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_BLOCK, mapName);
        } else {
            locationManager.setLocation(player, displayName, player.getLocation(), LocationState.NORMAL, mapName);
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
