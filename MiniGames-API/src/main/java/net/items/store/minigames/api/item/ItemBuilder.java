package net.items.store.minigames.api.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.location.ILocationManager;
import net.items.store.minigames.api.location.LocationState;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class ItemBuilder {

    private String displayName = null;
    private String skullValue = null;
    private Material material = null;
    private int amount = 1;
    private Color color = null;
    private boolean unbreakable = false;
    private Map<Enchantment, Integer> enchantments = Maps.newHashMap();
    private List<String> lore = Lists.newArrayList();
    private PotionEffectType potionEffectType;
    private int potionDuration;
    private int potionAmplifier;

    public static ItemBuilder modify() {
        return new ItemBuilder();
    }

    public ItemBuilder setPotionEffectType(PotionEffectType potionEffectType) {
        this.potionEffectType = potionEffectType;
        return this;
    }

    public ItemBuilder setPotionAmplifier(int potionAmplifier) {
        this.potionAmplifier = potionAmplifier;
        return this;
    }

    public ItemBuilder setPotionDuration(int potionDuration) {
        this.potionDuration = potionDuration;
        return this;
    }

    public ItemBuilder setSkullValue(String skullValue) {
        this.skullValue = skullValue;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder replaceLore(String replacingData, String replaceToData){
        List<String> newLore = Lists.newArrayList();

        for (String data : this.lore){
            newLore.add(data.replace(replacingData, replaceToData));
        }

        this.lore = newLore;
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int value) {
        this.enchantments.put(enchantment, value);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder copy(){
        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder.setMaterial(material);
        itemBuilder.setAmount(amount);
        itemBuilder.setColor(color);
        itemBuilder.setDisplayName(displayName);
        itemBuilder.setLore(lore);
        itemBuilder.setSkullValue(skullValue);
        itemBuilder.setUnbreakable(unbreakable);
        return itemBuilder;
    }

    public ItemBuilder addMapData(String mapName){
        boolean normalLocationsSet = true;
        Map<String, Boolean> teamStringBooleanMap = Maps.newHashMap();

        ITeamManager teamManager = MiniGame.get(ITeamManager.class);
        IMapManager mapManager = MiniGame.get(IMapManager.class);
        ILocationManager locationManager = MiniGame.get(ILocationManager.class);

        for(GameTeam gameTeam : teamManager.getTeams()){
            teamStringBooleanMap.put(gameTeam.getTeamName(), true);
        }
        addLore("§8§m----------");
        for(String key : mapManager.getLocationStateMap().keySet()){
            LocationState locationState = mapManager.getLocationStateMap().get(key);

            switch(locationState){
                case NORMAL:
                    if(normalLocationsSet) {
                        if (locationManager.getLocation(key, LocationState.NORMAL, mapName) == null) {
                            normalLocationsSet = false;
                        }
                    }
                    break;
                case TEAM:
                    for(GameTeam gameTeam : teamManager.getTeams()) {
                        if(teamStringBooleanMap.get(gameTeam.getTeamName())) {
                            if (locationManager
                                    .getLocation(key, LocationState.TEAM, mapName, gameTeam.getTeamName()) == null) {
                                teamStringBooleanMap.put(gameTeam.getTeamName(), false);
                            }
                        }
                    }
                    break;
                case COUNT:
                    addLore("§7Count " + key + "§8: §e" + locationManager
                            .getLocations(key, LocationState.COUNT, mapName).size());
                    break;
                default:
                    break;
            }
        }
        addLore("§8§m----------");
        addLore("§7Normal Locations exists§8: §e" + (normalLocationsSet ? "§aJa" : "§cNein"));
        addLore("§8§m----------");

        for(String teamName : teamStringBooleanMap.keySet()){
            addLore("§7Team " + teamName + " Locations exists: " + (teamStringBooleanMap.get(teamName) ? "§aJa" : "§cNein"));
        }
        addLore("§8§m----------");
        return this;
    }

    public ItemBuilder setModifiedBuilder(Location location, String... stringDataArray){
        this.lore.add("§8§m----------");
        if (location == null) {
            this.lore.add("§cLocation nicht gesetzt!");
        } else {
            this.lore.add("§7World§8: §e" + location.getWorld().getName());
            this.lore.add("§7X§8: §e" + location.getX());
            this.lore.add("§7Y§8: §e" + location.getY());
            this.lore.add("§7Z§8: §e" + location.getZ());
            this.lore.add("§7Yaw§8: §e" + location.getYaw());
            this.lore.add("§7Pitch§8: §e" + location.getPitch());
        }
        this.lore.add("§8§m----------");

        if(stringDataArray.length == 1){
            this.lore.add("§7Klicke, um die Location §6" + stringDataArray[0] + " §7zu setzen§8.");
        } else if(stringDataArray.length >= 2){
            this.lore.add("§7Klicke, um die Location §6" + stringDataArray[0] + " §7für " + stringDataArray[1] + " §7zu setzen§8.");
        }
        return this;
    }

    public ItemStack buildItem() {
        ItemStack itemStack = null;

        try {
            if (material != null) {
                itemStack = new ItemStack(material, amount);
            } else {
                return new ItemStack(Material.AIR);
            }

            if (potionEffectType != null){
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                potionMeta.addCustomEffect
                        (
                                new PotionEffect(potionEffectType, potionDuration*20, potionAmplifier),
                                true
                        );
                itemStack.setItemMeta(potionMeta);
            } else if (color != null) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(color);
                itemStack.setItemMeta(leatherArmorMeta);
            } else if (skullValue != null) {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

                PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
                playerProfile.getTextures().setSkin(new URL(SKIN_URL + skullValue));

                skullMeta.setOwnerProfile(playerProfile);

                itemStack.setItemMeta(skullMeta);
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if(unbreakable) {
                itemMeta.setUnbreakable(true);
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }
            if(displayName != null && !displayName.equalsIgnoreCase("")) {
                itemMeta.setDisplayName(displayName);
            }
            if(lore != null && lore.size() != 0) {
                itemMeta.setLore(lore);
            }
            itemStack.setItemMeta(itemMeta);

            if(!enchantments.isEmpty()) {
                for(Enchantment enchantment : enchantments.keySet()) {
                    itemStack.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
                }
            }
        } catch (Exception exception){
            System.out.println(exception.getStackTrace());
        }

        return itemStack;
    }

    private static final String SKIN_URL = "http://textures.minecraft.net/texture/";
}
