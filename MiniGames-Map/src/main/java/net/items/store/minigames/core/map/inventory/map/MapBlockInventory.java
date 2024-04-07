package net.items.store.minigames.core.map.inventory.map;

import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.map.MapBlockDirection;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.core.head.SiteSwitchHeads;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.MapBlocks;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.MapManager;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public class MapBlockInventory extends AbstractCustomInventory {

    public MapBlockInventory() {
        super(MapInventoryIdentifier.MAP_IDENTIFIER_BLOCK, "§eMap §8- §6Blöcke", 6*9);
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
        int nextSite = 1;
        String mapName = "";

        if (objects.length > 0){
            mapName = objects[0].toString();
        }

        if (objects.length > 2){
            int currentSite = (int) objects[1];
            MapBlockDirection mapBlockDirection = (MapBlockDirection) objects[2];

            nextSite = (mapBlockDirection == MapBlockDirection.RIGHT ? currentSite + 1 : currentSite - 1);
        }

        if (mapManager == null){
            mapManager = MiniGame.get(MapManager.class);
        }

        inventory.setItem(4, ItemBuilder.modify().setMaterial(Material.GRASS_BLOCK)
                .setDisplayName("§6" + mapName).buildItem());
        inventory.setItem(49, ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§7Seite§8: §e" + nextSite).buildItem());

        if(nextSite > 1){
            inventory.setItem(45, SiteSwitchHeads.HEAD_SWITCH_LEFT);
        }

        if(nextSite < MapBlocks.getLastSite()) {
            inventory.setItem(53, SiteSwitchHeads.HEAD_SWITCH_RIGHT);
        }

        for(ItemStack itemStack : MapBlocks.getBlockMap().get(nextSite)){
            inventory.addItem(itemStack);
        }
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        IMapManager mapManager = MiniGame.get(MapManager.class);
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        Player player = (Player) inventoryClickEvent.getWhoClicked();
        String mapName = player.getOpenInventory().getItem(4).getItemMeta().clone()
                .getDisplayName().replace("§6", "");
        String displayName = inventoryClickEvent.getCurrentItem().getItemMeta().clone()
                .getDisplayName().replace("§e", "");
        IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);

        if(inventoryClickEvent.getSlot() < 9 && inventoryClickEvent.getSlot() > 44){
            String currentSiteAsString = player.getOpenInventory().getItem(49)
                    .getItemMeta().getDisplayName().split(" ")[1];
            int currentSite = Integer.valueOf(currentSiteAsString.replace("§e", ""));

            if(displayName.equalsIgnoreCase("Nächste Seite")){
                inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_BLOCK, mapName, currentSite, MapBlockDirection.RIGHT);
            } else if(displayName.equalsIgnoreCase("Vorherige Seite")){
                inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_BLOCK, mapName, currentSite, MapBlockDirection.LEFT);
            }
        } else if (inventoryClickEvent.getSlot() > 8 && inventoryClickEvent.getSlot() < 45) {
            Optional<GameMap> optionalGameMap = mapManager.getMaps().stream()
                    .filter(x -> x.getMapName().equalsIgnoreCase(mapName))
                    .findFirst();
            if (optionalGameMap.isPresent()) {
                GameMap gameMap = optionalGameMap.get();
                gameMap.setMapMaterial(inventoryClickEvent.getCurrentItem().getType());

                Map<Object, Object> objectObjectMap = Maps.newHashMap();
                objectObjectMap.put("{MAP}", mapName);

                player.sendMessage(messageManager
                        .getMessage("UpdatedBlock", objectObjectMap));

                mapManager.updateGameMaps();
                inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_LOCATION, mapName);
            }
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

    private static MapManager mapManager;
}
