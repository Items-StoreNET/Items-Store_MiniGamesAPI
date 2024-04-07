package net.items.store.minigames.core.map.inventory.location;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.MapManager;
import net.items.store.minigames.core.map.location.LocationInventoryIdentifier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultLocationInventory extends AbstractCustomInventory {

    public DefaultLocationInventory(){
        super(LocationInventoryIdentifier.LOCATION_IDENTIFIER_DEFAULT, "§eLocations", 9);
    }

    @Override
    public void fillDefaultItems(Inventory inventory) {
        IMapManager mapManager = MiniGame.get(MapManager.class);

        if(mapManager.getLocationStateMap().values().contains(LocationState.LOBBY)) {
            ILocationManager locationManager = MiniGame.get(ILocationManager.class);
            Location lobbyLocation = locationManager.getLocation("Lobby", LocationState.LOBBY);
            ItemBuilder itemBuilder = ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§aLobby")
                    .setModifiedBuilder(lobbyLocation, "§6Lobby");
            inventory.setItem(0, itemBuilder.buildItem());
        }
        inventory.setItem(1, ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§aMaps")
                .addLore("").addLore("§7Klicke, um die §6Maps §7zu öffnen.").buildItem());
    }

    @Override
    public void fillCustomItems(Player player, Inventory inventory, Object... objects) {
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player) inventoryClickEvent.getWhoClicked();
        String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone().getDisplayName();
        ILocationManager locationManager = MiniGame.get(ILocationManager.class);

        if(displayName.equalsIgnoreCase("§aLobby")) {
            locationManager.setLocation(player, "Lobby", player.getLocation(), LocationState.LOBBY);
        } else if(displayName.equalsIgnoreCase("§aMaps")) {
            IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
            inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_DEFAULT);
        }

    }

    @Override
    public String getChangedInventoryName(Player player, Object... objects) {
        return inventoryName;
    }

    @Override
    public ItemStack getInventoryOpenItem(Player player) {
        return null;
    }
}
