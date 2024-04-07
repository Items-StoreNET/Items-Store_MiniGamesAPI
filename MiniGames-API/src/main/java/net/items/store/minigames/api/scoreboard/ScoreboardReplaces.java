package net.items.store.minigames.api.scoreboard;

public enum ScoreboardReplaces {

    MAP( "{MAP}"),
    KILLS("{KILLS}"),
    DEATHS("{DEATHS}"),
    KIT("{KIT}"),
    TEAM("{TEAM}"),
    KD("{KD}"),
    POINTS("{POINTS}"),
    WINS("{WINS}"),
    LOSES("{LOSES}"),
    COUNT("{COUNT}"),
    ONLINE_PLAYERS("{ONLINE_PLAYERS}"),
    PLAYER_ALIVE("{PLAYER_ALIVE}"),
    MAX_PLAYERS("{MAX_PLAYERS}"),
    MIN_PLAYERS("{MIN_PLAYERS}"),
    TOKENS("{TOKENS}"),
    PLAYERS("{PLAYERS}");

    private String replaceString;

    ScoreboardReplaces(String replaceString){
        this.replaceString = replaceString;
    }

    public String getReplaceString() {
        return replaceString;
    }
}
