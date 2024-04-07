package net.items.store.minigames.core.map.inventory.map;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.AbstractCustomInventory;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.MapManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultMapInventory extends AbstractCustomInventory {

    public DefaultMapInventory(){
        super(MapInventoryIdentifier.MAP_IDENTIFIER_DEFAULT, "§eMaps", 6*9);
    }

    @Override
    public void fillDefaultItems(Inventory inventory) {
        for(int i = 0; i < 9; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§0").buildItem());
        }
        for(int i = 45; i < 54; i++){
            inventory.setItem(i, ItemBuilder.modify().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§0").buildItem());
        }

        inventory.setItem(4, ItemBuilder.modify().setMaterial(Material.GRASS_BLOCK).setDisplayName("§eMaps").buildItem());
        inventory.setItem(49, ItemBuilder.modify().setMaterial(Material.PAPER).setDisplayName("§7Seite§8: §e1").buildItem());

        if (mapManager == null){
            mapManager = MiniGame.get(MapManager.class);
        }

        for(GameMap gameMap : mapManager.getMaps()){
            inventory.addItem(ItemBuilder.modify().setMaterial(gameMap.getMapMaterial())
                    .setDisplayName("§a" + gameMap.getMapName()).addMapData(gameMap.getMapName()).buildItem());
        }
    }

    @Override
    public void fillCustomItems(Player player, Inventory inventory, Object... objects) {
    }

    @Override
    public void performInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getSlot() > 8 && inventoryClickEvent.getSlot() < 45) {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            String mapName = inventoryClickEvent.getCurrentItem().getItemMeta().clone().getDisplayName();
            mapName = mapName.replace("§a", "");

            IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
            inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_LOCATION, mapName);
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
