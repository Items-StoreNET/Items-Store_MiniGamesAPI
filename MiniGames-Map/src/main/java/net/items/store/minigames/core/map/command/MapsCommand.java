package net.items.store.minigames.core.map.command;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.map.MapInventoryIdentifier;
import net.items.store.minigames.core.map.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(command.getName().equalsIgnoreCase("maps")){
                if(player.hasPermission("System.Maps")){
                    IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
                    inventoryManager.openInventory(player, MapInventoryIdentifier.MAP_IDENTIFIER_DEFAULT);
                }
            }
        }
        return false;
    }
}
