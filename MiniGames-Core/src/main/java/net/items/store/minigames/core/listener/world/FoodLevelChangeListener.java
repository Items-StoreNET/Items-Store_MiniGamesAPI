package net.items.store.minigames.core.listener.world;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent foodLevelChangeEvent){
        if(MiniGame.get(AbstractGame.class).getGameState() == GameState.LOBBY ||
                MiniGame.get(AbstractGame.class).getGameState() == GameState.RESTART) {
            foodLevelChangeEvent.setCancelled(true);
        }
    }
}
