package net.items.store.minigames.core.map.listener;

import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.map.MapBlockDirection;
import net.items.store.minigames.core.map.MapManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;
import java.util.Optional;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player) inventoryClickEvent.getWhoClicked();

        /*if (inventoryClickEvent.getClickedInventory() != null && player.getOpenInventory() != null) {
            MapManager mapManager = MiniGame.get(MapManager.class);
            ILocationManager locationManager = MiniGame.get(ILocationManager.class);

            if (player.getOpenInventory().getTitle().equalsIgnoreCase("§eMaps")) {
                inventoryClickEvent.setCancelled(true);

                if (inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null
                        && inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    if(inventoryClickEvent.getSlot() > 8 && inventoryClickEvent.getSlot() < 45) {
                        String mapName = inventoryClickEvent.getCurrentItem().getItemMeta().clone().getDisplayName();
                        mapName = mapName.replace("§a", "");

                        mapManager.openMapLocationInventory(player, mapName);
                    }
                    return;
                }
            } else if(player.getOpenInventory().getTitle().contains("§eMap")
                    && player.getOpenInventory().getTitle().toLowerCase().contains("voting") == false) {
                inventoryClickEvent.setCancelled(true);

                mapManager.getMapHandler().handleInventoryClickBlock(inventoryClickEvent);

                if (inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null &&
                        inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null &&
                        inventoryClickEvent.getClickedInventory() != player.getInventory()) {
                    String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone()
                            .getDisplayName().replace("§e", "");

                    if(player.getOpenInventory().getTitle().equalsIgnoreCase("§eMap §8- §6Blöcke")){
                        String mapName = player.getOpenInventory().getItem(4).getItemMeta().clone()
                                .getDisplayName().replace("§6", "");

                        if(inventoryClickEvent.getSlot() < 9 && inventoryClickEvent.getSlot() > 44){
                            String currentSiteAsString = player.getOpenInventory().getItem(49)
                                    .getItemMeta().getDisplayName().split(" ")[1];
                            int currentSite = Integer.valueOf(currentSiteAsString.replace("§e", ""));

                            if(displayName.equalsIgnoreCase("Nächste Seite")){
                                mapManager.openMapBlockInventory(player, mapName, currentSite, MapBlockDirection.RIGHT);
                            } else if(displayName.equalsIgnoreCase("Vorherige Seite")){
                                mapManager.openMapBlockInventory(player, mapName, currentSite, MapBlockDirection.LEFT);
                            }
                        }
                    } else {
                        if (player.getOpenInventory().getItem(4).getType() == Material.GRASS_BLOCK) {
                            String mapName = player.getOpenInventory().getItem(4).getItemMeta().clone()
                                    .getDisplayName().replace("§6", "");

                            if (displayName.contains("Count")) {
                                mapManager
                                        .openMapCountLocationInventory(player, mapName, displayName.split(" ")[1]);
                            } else if (displayName.contains("Team")) {
                                mapManager
                                        .openMapTeamLocationInventory(player, mapName, displayName.split(" ")[1]);
                            } else if (displayName.equals("Block ändern")) {
                                mapManager.openMapBlockInventory(player, mapName);
                            } else {
                                locationManager.setLocation(player,
                                        displayName,
                                        player.getLocation(), LocationState.NORMAL, mapName);
                            }
                        } else if (player.getOpenInventory().getItem(5).getItemMeta().getDisplayName().contains("§6Count")
                                || player.getOpenInventory().getItem(5).getItemMeta().getDisplayName().contains("§6Team")) {
                            String mapName = player.getOpenInventory().getItem(3).getItemMeta().clone()
                                    .getDisplayName().replace("§6", "");
                            String dataName = player.getOpenInventory().getItem(5).getItemMeta().clone()
                                    .getDisplayName().split(" ")[1];

                            if (inventoryClickEvent.getSlot() == 49 && inventoryClickEvent.getCurrentItem().getType() == Material.GOLD_INGOT) {
                                locationManager.setLocation(player, displayName.split(" ")[1],
                                        player.getLocation(), LocationState.COUNT, mapName);
                            } else {
                                if (mapManager
                                        .getLocationStateKeysFromState(LocationState.TEAM).contains(displayName)) {
                                    locationManager.setLocation(player, displayName,
                                            player.getLocation(), LocationState.TEAM, mapName, dataName);
                                }
                            }
                        }
                    }
                }
            } else if(player.getOpenInventory().getTitle().equalsIgnoreCase("§eLocations")) {
                inventoryClickEvent.setCancelled(true);

                if (inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null &&
                        inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null &&
                        inventoryClickEvent.getClickedInventory() != player.getInventory()) {
                    String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone().getDisplayName();

                    if(displayName.equalsIgnoreCase("§aLobby")) {
                        locationManager.setLocation(player, "Lobby",
                                player.getLocation(), LocationState.LOBBY);
                    } else if(displayName.equalsIgnoreCase("§aMaps")) {
                        mapManager.openMapsInventory(player);
                    }
                }
            }
        }*/
    }
}
