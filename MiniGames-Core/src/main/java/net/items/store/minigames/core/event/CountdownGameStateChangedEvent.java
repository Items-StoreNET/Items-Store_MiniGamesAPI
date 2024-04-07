package net.items.store.minigames.core.event;

import lombok.Getter;
import lombok.Setter;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.game.GameStateChangeReason;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownGameStateChangedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final GameState oldGameState;
    private final GameState newGameState;
    private final GameStateChangeReason reason;

    public CountdownGameStateChangedEvent(GameState oldGameState, GameState newGameState, GameStateChangeReason reason){
        this.oldGameState = oldGameState;
        this.newGameState = newGameState;
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public GameState getOldGameState() {
        return oldGameState;
    }

    public GameState getNewGameState() {
        return newGameState;
    }

    public GameStateChangeReason getReason() {
        return reason;
    }
}
