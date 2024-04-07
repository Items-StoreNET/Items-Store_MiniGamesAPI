package net.items.store.minigames.core.listener.countdown;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.countdown.AbstractCountdownTask;
import net.items.store.minigames.api.countdown.ICountdownManager;
import net.items.store.minigames.api.game.GameScoreboard;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.scoreboard.IScoreboardManager;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.event.CountdownGameStateChangedEvent;
import net.items.store.minigames.core.game.Game;
import net.items.store.minigames.core.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CountdownGameStateChangedListener implements Listener {

    @EventHandler
    public void onCountdownGameStateChanged(CountdownGameStateChangedEvent countdownGameStateChangedEvent){
        IScoreboardManager scoreboardManager = MiniGame.get(IScoreboardManager.class);
        if (scoreboardManager != null){
            GameScoreboard gameScoreboard = scoreboardManager
                    .findScoreboardByIdentifier(countdownGameStateChangedEvent.getNewGameState());

            if (gameScoreboard != null){
                for (Player player : Bukkit.getOnlinePlayers()){
                    scoreboardManager.sendScoreboardToPlayer(gameScoreboard, player, true);
                }
            }
        }
    }
}
