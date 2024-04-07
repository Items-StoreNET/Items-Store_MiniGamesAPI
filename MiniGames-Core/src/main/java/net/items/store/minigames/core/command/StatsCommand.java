package net.items.store.minigames.core.command;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.stats.IStatsManager;
import net.items.store.minigames.core.data.MapBuilder;
import net.items.store.minigames.core.message.MessageManager;
import net.items.store.minigames.core.uuid.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    public StatsCommand(){
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("StatsFalseArgue", "{PREFIX}§7/Stats%NL%{PREFIX}§7/Stats §8[§eName§8]");
        messageManager.addMessage("StatsMessageFirst", "{PREFIX}§7Stats von §e{PLAYER_NAME}§8:");
        messageManager.addMessage("StatsMessage", "{PREFIX}§7{STATS_NAME} §8» §e{STATS_VALUE}");
        messageManager.addMessage("StatsNotFound", "{PREFIX}§cDer angegebene Spieler hat noch nie gespielt§8.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            IMessageManager messageManager = MiniGame.get(MessageManager.class);

            if(command.getName().equalsIgnoreCase("Stats")){
                if(args.length == 0){
                    sendPlayerStats(player, player.getUniqueId(), messageManager);
                    return false;
                } else if(args.length == 1){
                    sendPlayerStats(player, UUIDFetcher.getUUID(args[0]), messageManager);
                    return false;
                }

                String[] messages = messageManager.getMessage("StatsFalseArgue").split("%NL%");

                for (int i = 0; i < messages.length; i++){
                    player.sendMessage(messages[i]);
                }
            }
        }
        return false;
    }

    private void sendPlayerStats(Player player, UUID uniqueID, IMessageManager messageManager){
        IStatsManager statsManager = MiniGame.get(IStatsManager.class);
        List<String> statsList = statsManager.getStatsStringList(uniqueID);

        if (statsList.size() == 0){
            player.sendMessage(messageManager.getMessage("StatsNotFound"));
            return;
        }

        String playerName = UUIDFetcher.getName(uniqueID);
        player.sendMessage(messageManager.getMessage("StatsMessageFirst",
                MapBuilder.getObjectMap(new Object[] { "{PLAYER_NAME}" }, new Object[] { playerName })));

        for (String key : statsList){
            player.sendMessage(messageManager.getMessage("StatsMessage",
                    MapBuilder.getObjectMap(
                            new Object[] { "{STATS_NAME}", "{STATS_VALUE}" },
                            new Object[] { key, statsManager.getStatsObject(uniqueID, key) })));
        }
    }
}
