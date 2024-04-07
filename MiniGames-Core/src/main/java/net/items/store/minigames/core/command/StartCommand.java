package net.items.store.minigames.core.command;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.countdown.AbstractCountdownTask;
import net.items.store.minigames.api.countdown.ICountdownManager;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    public StartCommand(){
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("GameAlreadyStarted", "{PREFIX}§cDas Game ist bereits gestartet.");
        messageManager.addMessage("GameSuccessfullyStarted", "{PREFIX}§aDas Game wurde erfolgreich gestartet§8.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(command.getName().equalsIgnoreCase("Start")){
                if(player.hasPermission("System.Start")){
                    AbstractGame abstractGame = MiniGame.get(AbstractGame.class);

                    if(abstractGame.getGameState() == GameState.LOBBY){
                        ICountdownManager countdownManager = MiniGame.get(ICountdownManager.class);
                        AbstractCountdownTask abstractCountdownTask = countdownManager.getCountdown(GameState.LOBBY);

                        if(abstractCountdownTask.getCount() > 15){
                            abstractCountdownTask.setCount(15);
                            player.sendMessage(MiniGame.get(MessageManager.class).getMessage("GameSuccessfullyStarted"));
                            return false;
                        }
                    }

                    player.sendMessage(MiniGame.get(MessageManager.class).getMessage("GameAlreadyStarted"));
                } else {
                    player.sendMessage(MiniGame.get(MessageManager.class).getMessage("NoPermission"));
                }
            }
        }
        return false;
    }
}
