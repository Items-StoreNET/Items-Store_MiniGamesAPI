package net.items.store.minigames.core.event;

import net.items.store.minigames.api.countdown.AbstractCountdownTask;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CountdownCountEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final AbstractCountdownTask countdownTask;

    public CountdownCountEvent(AbstractCountdownTask countdownTask){
        this.countdownTask = countdownTask;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public AbstractCountdownTask getCountdownTask() {
        return countdownTask;
    }
}
