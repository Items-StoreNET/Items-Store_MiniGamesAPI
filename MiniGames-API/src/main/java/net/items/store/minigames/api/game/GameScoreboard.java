package net.items.store.minigames.api.game;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.items.store.minigames.api.scoreboard.ScoreboardLine;
import net.items.store.minigames.api.scoreboard.ScoreboardReplaces;

import java.util.List;

@Getter
public class GameScoreboard {

    private GameState gameState;
    private String title;
    private List<ScoreboardLine> scoreboardLines;

    public GameScoreboard(GameState identifier, String title){
        this.gameState = identifier;
        this.title = title;
        this.scoreboardLines = Lists.newArrayList();
    }

    public void addScoreboardLine(int key, String line, List<ScoreboardReplaces> scoreboardReplaces){
        ScoreboardLine scoreboardLine = new ScoreboardLine(key, key, line, "", line, scoreboardReplaces);

        this.scoreboardLines.add(scoreboardLine);
    }

    public void addScoreboardLine(int key, String line, ScoreboardReplaces... scoreboardReplaces){
        List<ScoreboardReplaces> scoreboardReplaceList = Lists.newArrayList();
        if(scoreboardReplaces.length > 0) {
            scoreboardReplaceList = Lists.newArrayList(scoreboardReplaces);
        }
        addScoreboardLine(key, line, scoreboardReplaceList);
    }

    public String getIdentifier(){
        return gameState.getIdentifier();
    }

}
