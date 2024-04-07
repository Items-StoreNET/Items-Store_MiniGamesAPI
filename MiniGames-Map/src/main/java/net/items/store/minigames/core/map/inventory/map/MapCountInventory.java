package net.items.store.minigames.core.map.inventory.map;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.location.LocationManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MapCountInventory extends AbstractCustomInventory {

    public MapCountInventory() {
        super(MapInventoryIdentifier.MAP_IDENTIFIER_COUNT, "§eMap §8- §6{MAP_NAME}", 6*9);
    }

    @Override
    public void fillDefaultItems(Inventory inventory) {
        for(int i = 0; i < 9; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName("§0").buildItem());
        }
        for(int i = 45; i < 54; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setDisplayName("§0").buildItem());
        }
    }

    @Override
    public void fillCustomItems(Player player, Inventory inventory, Object... objects) {
        String countName = getDataAsString(1, objects);
        ILocationManager locationManager = MiniGame.get(LocationManager.class);

        inventory.setItem(3, ItemBuilder.modify().setMaterial(Material.GRASS_BLOCK).setDisplayName("§6" + getDataAsString(0, objects)).buildItem());
        inventory.setItem(5, ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§6Count " + countName).buildItem());
        inventory.setItem(49, ItemBuilder.modify().setMaterial(Material.GOLD_INGOT).setDisplayName("§aAdd " + countName + " Location").buildItem());

        for(Location location : locationManager.getLocations(countName, LocationState.COUNT, getDataAsString(0, objects))){
            inventory.addItem(ItemBuilder.modify().setMaterial(Material.GOLD_NUGGET).setDisplayName("§e" + countName).setModifiedBuilder(location).buildItem());
        }
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getSlot() == 49 && inventoryClickEvent.getCurrentItem().getType() == Material.GOLD_INGOT) {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            String mapName = player.getOpenInventory().getItem(3).getItemMeta().clone()
                    .getDisplayName().replace("§6", "");
            ILocationManager locationManager = MiniGame.get(ILocationManager.class);
            String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone()
                    .getDisplayName().replace("§e", "");

            locationManager.setLocation(player, displayName.split(" ")[1], player.getLocation(), LocationState.COUNT, mapName);
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
