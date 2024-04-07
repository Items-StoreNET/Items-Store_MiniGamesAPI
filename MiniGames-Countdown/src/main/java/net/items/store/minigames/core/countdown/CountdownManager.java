package net.items.store.minigames.core.countdown;

import com.google.common.collect.Lists;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.countdown.AbstractCountdownTask;
import net.items.store.minigames.api.countdown.ICountdownManager;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.core.countdown.task.DefaultCountdownTask;

import java.util.List;
import java.util.Optional;

public class CountdownManager implements ICountdownManager {

    private final List<AbstractCountdownTask> countdownTaskList;

    public CountdownManager(){
        this.countdownTaskList = Lists.newArrayList();
    }

    @Override
    public void scheduleCountdown() {
        MiniGame.getExecutorService().submit(() ->{
            while(true){
                for(AbstractCountdownTask countdownTask : countdownTaskList){
                    if(countdownTask.getGameState() == MiniGame.get(AbstractGame.class).getGameState()) {
                        countdownTask.executeEvent();
                    }
                }

                Thread.sleep(999);
            }
        });
    }

    @Override
    public void addCountdown(AbstractCountdownTask countdown) {
        this.countdownTaskList.add(countdown);
    }

    public List<AbstractCountdownTask> getCountdowns() {
        return countdownTaskList;
    }

    @Override
    public AbstractCountdownTask getCountdown(GameState gameState) {
        return this.countdownTaskList.stream()
                .filter(x -> x.getIdentifier().equalsIgnoreCase(gameState.getIdentifier())).findAny().orElse(null);
    }

    @Override
    public String getCountAsString(int count) {
        int seconds = count;
        int minutes = 0;

        while(seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        if(minutes > 9 && seconds > 9) {
            return minutes + ":" + seconds;
        } else if(minutes > 9 && seconds < 10) {
            return minutes + ":0" + seconds;
        } else if(minutes < 10 && seconds < 10) {
            return "0" + minutes + ":0" + seconds;
        } else if(minutes < 10 && seconds > 9) {
            return "0" + minutes + ":" + seconds;
        } else {
            return "NULL";
        }
    }
}