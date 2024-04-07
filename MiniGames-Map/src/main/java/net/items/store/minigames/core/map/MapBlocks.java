package net.items.store.minigames.core.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.items.store.minigames.api.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class MapBlocks {

    private static Map<Integer, List<ItemStack>> integerItemStackMap;
    private static int lastSite;

    static {
        integerItemStackMap = Maps.newHashMap();
        registerBlocks();
    }

    public static Map<Integer, List<ItemStack>> getBlockMap() {
        return integerItemStackMap;
    }

    public static int getLastSite() {
        return lastSite;
    }

    private static void registerBlocks(){
        Inventory inventory = Bukkit.createInventory(null, 1*9, "null");
        int site = 1;
        int count = 0;

        for(Material material : Material.values()){
            if(!integerItemStackMap.containsKey(site)) {
                integerItemStackMap.put(site, Lists.newArrayList());
            }
            inventory.addItem(ItemBuilder.modify().setMaterial(material).buildItem());
            if(inventory.contains(material)){
                integerItemStackMap.get(site).add(ItemBuilder.modify().setMaterial(material).buildItem());
                inventory.clear();

                if(++count >= 36){
                    count = 0;
                    site++;
                }
            }
        }
        lastSite = site;
    }
}
