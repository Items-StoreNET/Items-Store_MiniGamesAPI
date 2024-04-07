package net.items.store.minigames.core.map.command;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.core.map.MapManager;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(command.getName().equalsIgnoreCase("map")){
                IMessageManager messageManager = MiniGame.get(MessageManager.class);

                if(player.hasPermission("System.Map")){
                    if(args.length == 2){
                        if(args[0].equalsIgnoreCase("add")){
                            String mapName = args[1];
                            GameMap gameMap = new GameMap(mapName, Material.STONE, player.getWorld().getName());
                            IMapManager mapManager = MiniGame.get(MapManager.class);

                            mapManager.addMap(player, gameMap);
                            return false;
                        }
                    }

                    player.sendMessage(messageManager.getPrefix() + "§7/Map add [MapName]");
                } else {
                    player.sendMessage(messageManager.getMessage("NoPermission"));
                }
            }
        }
        return false;
    }
}
