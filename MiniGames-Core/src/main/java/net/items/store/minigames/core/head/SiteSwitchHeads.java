package net.items.store.minigames.core.head;

import net.items.store.minigames.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SiteSwitchHeads {

    public static final ItemStack HEAD_SWITCH_RIGHT = ItemBuilder.modify().setMaterial(Material.PLAYER_HEAD)
            .setDisplayName("§eNächste Seite")
            .setSkullValue("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf").buildItem();
    public static final ItemStack HEAD_SWITCH_LEFT = ItemBuilder.modify().setMaterial(Material.PLAYER_HEAD)
            .setDisplayName("§eVorherige Seite")
            .setSkullValue("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9").buildItem();

}