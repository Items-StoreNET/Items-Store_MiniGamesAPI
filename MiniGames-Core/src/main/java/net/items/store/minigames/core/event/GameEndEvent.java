package net.items.store.minigames.core.event;

import net.items.store.minigames.api.game.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class GameEndEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final GameState gameState;
    private final List<UUID> alivePlayers;

    public GameEndEvent(GameState gameState, List<UUID> alivePlayers){
        this.gameState = gameState;
        this.alivePlayers = alivePlayers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<UUID> getAlivePlayers() {
        return alivePlayers;
    }
}
