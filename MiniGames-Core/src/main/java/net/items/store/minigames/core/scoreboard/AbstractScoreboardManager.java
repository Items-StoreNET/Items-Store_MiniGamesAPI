package net.items.store.minigames.core.scoreboard;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.GameScoreboard;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.scoreboard.IScoreboardManager;
import net.items.store.minigames.api.scoreboard.ScoreboardLine;
import net.items.store.minigames.api.scoreboard.ScoreboardReplaces;
import net.items.store.minigames.core.data.FileBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class AbstractScoreboardManager implements IScoreboardManager {
	
	private List<GameScoreboard> gameScoreboardList;
    private Map<UUID, List<ScoreboardLine>> scoreboardSaveMap;

    public AbstractScoreboardManager(){
        this.gameScoreboardList = Lists.newArrayList();
        this.scoreboardSaveMap = Maps.newHashMap();

        loadScoreboards();
    }

    public void addScoreboard(GameScoreboard gameScoreboard) {
        this.gameScoreboardList.add(gameScoreboard);
    }

    public void sendScoreboardToPlayer(GameScoreboard gameScoreboard, Player player, boolean isNew) {
    	Bukkit.getScheduler().callSyncMethod(MiniGame.getJavaPlugin(), () ->{
            Scoreboard scoreboard = getPlayerScoreboard(player, isNew);
            Objective objective = getPlayerScoreboardObjective(scoreboard, gameScoreboard, player, isNew);
            List<ScoreboardLine> scoreboardLines = getLines(player, gameScoreboard);

            for(ScoreboardLine scoreboardLine : scoreboardLines){
                objective.getScore(scoreboardLine.getLine()).setScore(scoreboardLine.getKey());
            }
            scoreboardSaveMap.put(player.getUniqueId(), scoreboardLines);

            player.setScoreboard(scoreboard);
    		return null;
    	});
    }
    
    public void removePlayerScoreboard(Player player) {
    	if(scoreboardSaveMap.containsKey(player.getUniqueId())) {
    		scoreboardSaveMap.remove(player.getUniqueId());
    	}
    }

    public void updateScoreboardForPlayer(GameScoreboard gameScoreboard, Player player) {
    	if(this.scoreboardSaveMap.containsKey(player.getUniqueId())) {
	        Scoreboard scoreboard = getPlayerScoreboard(player, false);
	        Objective objective = getPlayerScoreboardObjective(scoreboard, gameScoreboard, player, false);
	        List<ScoreboardLine> scoreboardLines = getLines(player, gameScoreboard);
	        
	        String replacedTitle = getReplacedTitle(gameScoreboard.getTitle());
	        if(objective.getDisplayName().equalsIgnoreCase(replacedTitle) == false) {
	        	objective.setDisplayName(replacedTitle);
	        }
	
	        for(ScoreboardLine scoreboardLine : this.scoreboardSaveMap.get(player.getUniqueId())) {
	        	String line = scoreboardLine.getLine();
	        	long defaultLineCount = scoreboardLines.stream()
	        			.filter(y -> y.getDefaultLine().equalsIgnoreCase(scoreboardLine.getDefaultLine())).count();
	        	
	            if(objective.getScore(line) != null) {
	                List<ScoreboardLine> currentScoreboardLines = scoreboardLines.stream()
	                		.filter(x -> x.getDefaultLine().equalsIgnoreCase(scoreboardLine.getDefaultLine()) && 
	                				x.getDefaultKey() == scoreboardLine.getDefaultKey() &&
	                				(
	                					(
	                						x.getData().equalsIgnoreCase(scoreboardLine.getData()) == false ||
	                						x.getLine().equalsIgnoreCase(scoreboardLine.getLine()) == false ||
	                						x.getKey() == scoreboardLine.getKey() == false
	                					) 
	                					&& defaultLineCount <= 1 ||
	                					(
	                						x.getData().equalsIgnoreCase(scoreboardLine.getData()) == true &&
	                						x.getKey() == scoreboardLine.getKey() == false
	                					)
	                				))
	                		.collect(Collectors.toList());
	                
	                if(currentScoreboardLines.size() > 0) {
	                	ScoreboardLine currentLine = currentScoreboardLines.get(0);
	                	
	                	scoreboard.resetScores(line);
	                	objective.getScore(currentLine.getLine()).setScore(currentLine.getKey());
	                }
	            }
	        }
	        for(ScoreboardLine scoreboardLine : this.scoreboardSaveMap.get(player.getUniqueId())) {
	        	if(scoreboardLines.stream().filter(x -> x.getLine().equalsIgnoreCase(scoreboardLine.getLine())).count() == 0) {
	        		scoreboard.resetScores(scoreboardLine.getLine());
	        	}
	        }
	        scoreboardSaveMap.put(player.getUniqueId(), scoreboardLines);
	
	        player.setScoreboard(scoreboard);
    	}
    }

    public List<GameScoreboard> getScoreboardList() {
        return this.gameScoreboardList;
    }

    public GameScoreboard findScoreboardByIdentifier(GameState gameState) {
        Optional<GameScoreboard> optionalGameScoreboard = this.gameScoreboardList
                .stream().filter(x -> x.getIdentifier().equalsIgnoreCase(gameState.getIdentifier())).findFirst();

        if(optionalGameScoreboard.isPresent()){
            return optionalGameScoreboard.get();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public Objective getPlayerScoreboardObjective(Scoreboard scoreboard, GameScoreboard gameScoreboard, Player player, boolean isNew) {
        Objective objective;

        if(isNew == true){
            objective = scoreboard.registerNewObjective("123", "456");
            objective.setDisplayName(getReplacedTitle(gameScoreboard.getTitle()));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        }
        return objective;
    }

    public Scoreboard getPlayerScoreboard(Player player, boolean isNew) {
        Scoreboard scoreboard;

        if(player.getScoreboard() != null){
            Scoreboard oldScoreboard = player.getScoreboard();

            if(isNew) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

                for (Team team : oldScoreboard.getTeams()) {
                    Team currentTeam = scoreboard.registerNewTeam(team.getName());
                    currentTeam.setPrefix(team.getPrefix());
                    currentTeam.setSuffix(team.getSuffix());
                    currentTeam.setDisplayName(team.getDisplayName());
                    currentTeam.setAllowFriendlyFire(team.allowFriendlyFire());
                    currentTeam.setCanSeeFriendlyInvisibles(team.canSeeFriendlyInvisibles());
                }
            } else {
                scoreboard = oldScoreboard;
            }
        } else {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        return scoreboard;
    }

    public GameScoreboard getGameScoreboard(GameState gameState) {
		Optional<GameScoreboard> optionalGameScoreboard = gameScoreboardList.stream()
				.filter(x -> x.getIdentifier().equals(gameState.getIdentifier())).findFirst();
		
		if(optionalGameScoreboard.isPresent()) {
			return optionalGameScoreboard.get();
		}
		return null;
	}

	protected abstract String getReplacedTitle(String title);
	
	protected abstract List<ScoreboardLine> getLines(Player player, GameScoreboard gameScoreboard);

	public abstract String getReplacedLine(Player player, String line, ScoreboardReplaces scoreboardReplaces);

	private void loadScoreboards(){
        JSONArray jsonArray = FileBuilder.loadJSONArray("scoreboards.json", true);

        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            JSONArray scoreboardLinesArray = (JSONArray) jsonObject.get("scoreboardLines");

            GameState gameState = GameState.findByIdentifier(jsonObject.get("identifier").toString());
            String title = jsonObject.get("title").toString();

            GameScoreboard gameScoreboard = new GameScoreboard(gameState, title);
            for(int b = 0; b < scoreboardLinesArray.size(); b++){
                JSONObject scoreboardLineObject = (JSONObject) scoreboardLinesArray.get(b);
                int key = Integer.valueOf(scoreboardLineObject.get("key").toString());
                String line = scoreboardLineObject.get("line").toString();

                gameScoreboard.addScoreboardLine(key, line);
            }
            addScoreboard(gameScoreboard);
        }
    }
	
}
