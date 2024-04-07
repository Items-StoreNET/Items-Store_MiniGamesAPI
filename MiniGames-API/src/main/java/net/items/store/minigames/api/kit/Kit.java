package net.items.store.minigames.api.kit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.items.store.minigames.api.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
public class Kit {

    private String kitName;
    private int kitPrice;
    private boolean kitDefault;
    private String kitFreePermission;
    private ItemBuilder kitMainItem;
    @Setter
    private Map<Integer, List<ItemStack>> kitItemList;

    public Kit(String kitName, String kitFreePermission, int kitPrice, boolean kitDefault, ItemBuilder kitMainItem){
        this.kitName = kitName;
        this.kitFreePermission = kitFreePermission;
        this.kitPrice = kitPrice;
        this.kitDefault = kitDefault;
        this.kitItemList = Maps.newHashMap();
        this.kitMainItem = kitMainItem;
    }

    public void addItem(Integer slot, ItemStack itemStack){
        if (kitItemList.containsKey(slot) == false){
            kitItemList.put(slot, Lists.newArrayList());
        }
        kitItemList.get(slot).add(itemStack);
    }
}
