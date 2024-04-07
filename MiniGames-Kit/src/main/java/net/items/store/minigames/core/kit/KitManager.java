package net.items.store.minigames.core.kit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.kit.IPlayerKitManager;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.core.data.DataBuilder;
import net.items.store.minigames.core.data.FileBuilder;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.kit.inventory.DefaultKitInventory;
import net.items.store.minigames.core.kit.inventory.KitBuyInventory;
import net.items.store.minigames.core.kit.inventory.KitUseInventory;
import net.items.store.minigames.core.kit.listener.InventoryClickListener;
import net.items.store.minigames.core.kit.listener.KitGameStateChangedListener;
import net.items.store.minigames.core.kit.listener.PlayerInteractListener;
import net.items.store.minigames.core.kit.listener.PlayerJoinListener;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KitManager implements IKitManager {

    private List<Kit> kitList;
    private String inventoryName;
    private Integer inventorySize;
    private ItemStack kitInventoryItem;
    private int kitInventorySlot;
    private boolean kitInventoryActive;

    public KitManager(){
        MiniGame.getJavaPlugin().getServer().getPluginManager()
                .registerEvents(new PlayerInteractListener(), MiniGame.getJavaPlugin());
        MiniGame.getJavaPlugin().getServer().getPluginManager()
                .registerEvents(new InventoryClickListener(), MiniGame.getJavaPlugin());
        MiniGame.getJavaPlugin().getServer().getPluginManager()
                .registerEvents(new PlayerJoinListener(), MiniGame.getJavaPlugin());
        MiniGame.getJavaPlugin().getServer().getPluginManager()
                .registerEvents(new KitGameStateChangedListener(), MiniGame.getJavaPlugin());

        this.kitList = Lists.newArrayList();
        this.kitInventoryActive = false;
        this.kitInventoryItem = null;
        this.kitInventorySlot = 0;

        loadKits();
    }

    @Override
    public void addKit(Kit kit) {
        this.kitList.add(kit);
    }


    @Override
    public List<Kit> getKits() {
        return kitList;
    }

    @Override
    public Kit getKitFromItemStack(ItemStack itemStack) {
        Kit kit = null;

        for(Kit currentKit : kitList){
            ItemBuilder mainItem = currentKit.getKitMainItem();

            if(itemStack.getItemMeta().getDisplayName().contains(currentKit.getKitName()) &&
                    mainItem.getMaterial().name().equalsIgnoreCase(itemStack.getType().name())){
                kit = currentKit;
                break;
            }
        }
        return kit;
    }

    @Override
    public Kit getKitFromName(String kitName) {
        Kit kit = null;

        for(Kit currentKit : kitList){
            if (currentKit.getKitName().equalsIgnoreCase(kitName)){
                kit = currentKit;
                break;
            }
        }

        return kit;
    }

    @Override
    public Kit getDefaultKit() {
        Kit kit = null;

        for(Kit currentKit : kitList){
            if(currentKit.isKitDefault()){
                kit = currentKit;
                break;
            }
        }
        return kit;
    }

    @Override
    public void updateKitInventoryData(ItemStack kitInventoryItem, int kitInventorySlot, boolean kitInventoryActive) {
        this.kitInventoryItem = kitInventoryItem;
        this.kitInventorySlot = kitInventorySlot;
        this.kitInventoryActive = kitInventoryActive;
    }

    @Override
    public boolean compareItems(ItemStack itemStack) {
        if(this.kitInventoryItem != null){
            if(itemStack.getType() == this.kitInventoryItem.getType()
                && itemStack.getItemMeta().getDisplayName()
                    .equalsIgnoreCase(this.kitInventoryItem.getItemMeta().getDisplayName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setItemToInventory(Player player) {
        if(this.kitInventoryActive){
            if(this.kitInventoryItem != null){
                player.getInventory().setItem(this.kitInventorySlot, this.kitInventoryItem);
            }
        }
    }

    @Override
    public void givePlayerKit(Player player, Kit kit) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        Bukkit.getScheduler().callSyncMethod(MiniGame.getJavaPlugin(), () ->{
            for (Integer slot : kit.getKitItemList().keySet()){
                List<ItemStack> itemStacks = kit.getKitItemList().get(slot);

                if (itemStacks.size() > 0) {
                    switch (slot) {
                        case -1:
                            player.getInventory().setHelmet(itemStacks.get(0));
                            break;
                        case -2:
                            player.getInventory().setChestplate(itemStacks.get(0));
                            break;
                        case -3:
                            player.getInventory().setLeggings(itemStacks.get(0));
                            break;
                        case -4:
                            player.getInventory().setBoots(itemStacks.get(0));
                            break;
                        case -5:
                            for (ItemStack itemStack : itemStacks) {
                                player.getInventory().addItem(itemStack);
                            }
                            break;
                        default:
                            player.getInventory().setItem(slot, itemStacks.get(0));
                            break;
                    }
                }
            }
            return null;
        });
    }

    @Override
    public String getInventoryName() {
        return this.inventoryName;
    }

    @Override
    public void registerDefault() {
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("KitInventory", "§eKits");
        messageManager.addMessage("KitInventorySize", "54");
        messageManager.addMessage("KitActive", " §8(§aAktiviert§8)");
        messageManager.addMessage("KitBuyed", " §8(§aGekauft§8)");
        messageManager.addMessage("KitNotBuyed", " §8(§cNicht gekauft§8)");

        messageManager.addMessage("KitBuyDisplayName", "§cKit kaufen");
        messageManager.addMessage("KitChangeDisplayName", "§aKit wählen");

        messageManager.addMessage("KitBuyClose", "§cSchließen");

        messageManager.addMessage("KitBuyLore", "§7Preis§8: §e{COINS} Coins");
        messageManager.addMessage("KitChangeLore", "§7Klicke hier, um das Kit zu wählen.");

        this.inventoryName = messageManager.getMessage("KitInventory");
        this.inventorySize = Integer.valueOf(messageManager.getMessage("KitInventorySize"));

        IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
        inventoryManager.addCustomInventory(new DefaultKitInventory(this.kitInventoryItem, this.inventoryName, this.inventorySize));
        inventoryManager.addCustomInventory(new KitBuyInventory());
        inventoryManager.addCustomInventory(new KitUseInventory());
    }

    private void loadKits(){
        JSONObject kitJSONObject = FileBuilder.loadJSONObject("kits.json", true);

        if (kitJSONObject.containsKey("inventoryItem")){
            ItemStack kitInventoryItem = DataBuilder.getItemBuilderFromJSONObject((JSONObject) kitJSONObject.get("inventoryItem")).buildItem();
            int kitInventorySlot = Integer.valueOf(kitJSONObject.get("inventorySlot").toString());
            boolean kitInventoryActive = Boolean.valueOf(kitJSONObject.get("inventoryItemActive").toString());

            updateKitInventoryData(kitInventoryItem, kitInventorySlot, kitInventoryActive);
        }

        JSONArray jsonArray = (JSONArray) kitJSONObject.get("kits");
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String kitName = jsonObject.get("name").toString();
            String freePermission = jsonObject.get("freePermission").toString();
            boolean kitIsDefault = Boolean.valueOf(jsonObject.get("default").toString());
            int kitPrice = Integer.valueOf(jsonObject.get("price").toString());
            ItemBuilder mainItemBuild = DataBuilder.getItemBuilderFromJSONObject((JSONObject) jsonObject.get("overviewItem"));
            Kit kit = new Kit(kitName, freePermission, kitPrice, kitIsDefault, mainItemBuild);
            JSONArray itemJSONArray = (JSONArray) jsonObject.get("items");

            for(int itemCount = 0; itemCount < itemJSONArray.size(); itemCount++){
                JSONObject itemJSONObject = (JSONObject) itemJSONArray.get(itemCount);
                ItemBuilder itemBuilder = DataBuilder.getItemBuilderFromJSONObject(itemJSONObject);
                int itemSlot = Integer.valueOf(itemJSONObject.get("slot").toString());

                kit.addItem(itemSlot, itemBuilder.buildItem());
            }
            addKit(kit);
        }
    }
}
