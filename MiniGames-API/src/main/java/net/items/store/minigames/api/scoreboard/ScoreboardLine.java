package net.items.store.minigames.api.scoreboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ScoreboardLine {

	@Setter
	private int key;
	private int defaultKey;
	@Setter
	private String line;
	@Setter
	private String data;
	private String defaultLine;
	private List<ScoreboardReplaces> scoreboardReplaces;
	
	public ScoreboardLine clone() {
		ScoreboardLine scoreboardLine = new ScoreboardLine(key, defaultKey, line, data, defaultLine, scoreboardReplaces);
		return scoreboardLine;
	}

}
