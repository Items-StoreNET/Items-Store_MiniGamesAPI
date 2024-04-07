package net.items.store.minigames.core.scoreboard;

import com.google.common.collect.Lists;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.countdown.AbstractCountdownTask;
import net.items.store.minigames.api.countdown.ICountdownManager;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameScoreboard;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.IPlayerKitManager;
import net.items.store.minigames.api.kit.PlayerKit;
import net.items.store.minigames.api.map.GameMap;
import net.items.store.minigames.api.map.IMapManager;
import net.items.store.minigames.api.scoreboard.ScoreboardLine;
import net.items.store.minigames.api.scoreboard.ScoreboardReplaces;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class DefaultScoreboardManager extends AbstractScoreboardManager {

    public DefaultScoreboardManager(){
        super();
    }

    @Override
    protected List<ScoreboardLine> getLines(Player player, GameScoreboard gameScoreboard){
        List<ScoreboardLine> scoreboardLineList = Lists.newArrayList();

        for(ScoreboardLine scoreboardLine : gameScoreboard.getScoreboardLines()){
            ScoreboardLine currentLine = scoreboardLine.clone();

            for(ScoreboardReplaces scoreboardReplaces : ScoreboardReplaces.values()){
                if(currentLine.getLine().contains(scoreboardReplaces.getReplaceString())) {
                    currentLine.setLine(getReplacedLine(player, currentLine.getLine(), scoreboardReplaces));
                    currentLine.setData(currentLine.getLine());
                }
            }
            scoreboardLineList.add(currentLine);
        }
        return scoreboardLineList;
    }

    @Override
    protected String getReplacedTitle(String title) {
        String newTitle = title;
        ICountdownManager countdownManager = MiniGame.get(ICountdownManager.class);

        newTitle = newTitle.replace(ScoreboardReplaces.COUNT.getReplaceString(),
                countdownManager.getCountAsString(getCount()));
        return newTitle;
    }

    @Override
    public String getReplacedLine(Player player, String line, ScoreboardReplaces scoreboardReplaces){
        switch (scoreboardReplaces){
            case COUNT:
                line = line.replace(scoreboardReplaces.getReplaceString(), String.valueOf(getCount()));
                break;
            case MAX_PLAYERS:
                line = line.replace(scoreboardReplaces.getReplaceString(),
                        String.valueOf(MiniGame.get(AbstractGame.class).getMaxPlayers()));
                break;
            case MIN_PLAYERS:
                line = line.replace(scoreboardReplaces.getReplaceString(),
                        String.valueOf(MiniGame.get(AbstractGame.class).getMinPlayers()));
                break;
            case ONLINE_PLAYERS:
                line = line.replace(scoreboardReplaces.getReplaceString(),
                        String.valueOf(Bukkit.getOnlinePlayers().size()));
                break;
            case MAP:
                IMapManager mapManager = MiniGame.get(IMapManager.class);
                if(mapManager != null){
                    GameMap gameMap = mapManager.getCurrentMap();

                    line = line.replace(scoreboardReplaces.getReplaceString(),
                            gameMap == null ? "MapVoting" : gameMap.getMapName());
                }
                break;
            case TEAM:
                ITeamManager teamManager = MiniGame.get(ITeamManager.class);
                if(teamManager != null){
                    GameTeam gameTeam = teamManager.getPlayerTeam(player);

                    line = line.replace(scoreboardReplaces.getReplaceString(),
                            gameTeam == null ? "§c✗" : gameTeam.getTeamPrefix() + gameTeam.getTeamName());
                }
                break;
            case KIT:
                IPlayerKitManager playerKitManager = MiniGame.get(IPlayerKitManager.class);
                if(playerKitManager != null){
                    PlayerKit playerKit = playerKitManager.getPlayerKit(player);

                    line = line.replace(scoreboardReplaces.getReplaceString(),
                            playerKit == null ? "§c✗" : playerKit.getKitName());
                }
                break;
            default:
                break;
        }
        return line;
    }

    @Override
    public void updateScoreboardTeam(Player player) {

    }

    private int getCount() {
        ICountdownManager countdownManager = MiniGame.get(ICountdownManager.class);
        AbstractCountdownTask abstractCountdownTask = countdownManager
                .getCountdown(MiniGame.get(AbstractGame.class).getGameState());

        if(abstractCountdownTask != null){
            return abstractCountdownTask.getCount();
        }
        return 0;
    }
}
