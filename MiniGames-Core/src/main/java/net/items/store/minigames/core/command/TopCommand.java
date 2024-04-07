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

public class TopCommand implements CommandExecutor {

    public TopCommand() {
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("TopMessage", "{PREFIX}§7{STATS_NAME} §8» §eRank: §6{STATS_VALUE}");
        messageManager.addMessage("TopNotFound", "{PREFIX}§cEs hat bisher noch niemand gespielt§8.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            IMessageManager messageManager = MiniGame.get(MessageManager.class);

            MiniGame.getExecutorService().submit(() ->{
                if (command.getName().equalsIgnoreCase("top")){
                    IStatsManager statsManager = MiniGame.get(IStatsManager.class);
                    List<Map.Entry<UUID, Integer>> topTen = statsManager.loadTopTen();

                    for (Map.Entry<UUID, Integer> top : topTen){
                        String playerName = UUIDFetcher.getName(top.getKey());

                        player.sendMessage(messageManager.getMessage("TopMessage",
                                MapBuilder.getObjectMap(
                                        new Object[] { "{STATS_NAME}", "{STATS_VALUE}" },
                                        new Object[] { playerName, top.getValue() })));
                    }
                }
            });
        }
        return false;
    }
}
