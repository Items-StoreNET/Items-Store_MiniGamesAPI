package net.items.store.minigames.core.command;

import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.player.IPlayerManager;
import net.items.store.minigames.core.message.MessageManager;
import net.items.store.minigames.core.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class BuildCommand implements CommandExecutor {


    public BuildCommand(){
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("BuildCommand", "{PREFIX}§7Du hast den §6Build-Modus {ACTIVE}§8.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(command.getName().equalsIgnoreCase("build")){
                if(player.hasPermission("System.Build")){
                    IPlayerManager playerManager = MiniGame.get(PlayerManager.class);

                    boolean active = playerManager.canPlayerBuild(player.getUniqueId());
                    active = active ? false : true;

                    playerManager.setPlayerCanBuild(player.getUniqueId(), active);

                    Map<Object, Object> objectObjectMap = Maps.newHashMap();
                    objectObjectMap.put("{ACTIVE}", active ? "§aaktiviert" : "§cdeaktiviert");

                    player.sendMessage(MiniGame.get(MessageManager.class).getMessage("BuildCommand", objectObjectMap));
                } else {
                    player.sendMessage(MiniGame.get(MessageManager.class).getMessage("NoPermission"));
                }
            }
        }
        return false;
    }
}
