package net.items.store.minigames.core.countdown.task;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.countdown.AbstractCountdownTask;
import net.items.store.minigames.api.countdown.CountDirection;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.core.event.CountdownCountEvent;
import org.bukkit.Bukkit;

public class DefaultCountdownTask extends AbstractCountdownTask {

    public DefaultCountdownTask(String identifier, GameState gameState, int count){
        super(identifier, gameState, count, CountDirection.DOWN);
    }

    @Override
    public void executeEvent() {
        MiniGame.getExecutorService().submit(() ->{
            Bukkit.getScheduler().callSyncMethod(MiniGame.getJavaPlugin(), () ->{
                CountdownCountEvent countdownCountEvent = new CountdownCountEvent(this);

                MiniGame.getJavaPlugin().getServer().getPluginManager().callEvent(countdownCountEvent);
                return null;
            });
        });
    }
}
