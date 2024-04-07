package net.items.store.minigames.api.game;

import com.google.common.collect.Lists;

import java.util.Optional;

public enum GameState {
	
	LOBBY("Lobby"),
	WAITING("Waiting"),
	INGAME("InGame"),
	SHOP("Shop"),
	DEATHMATCH("DeathMatch"),
	RESTART("Restart");

	private String identifier;

	GameState(String identifier){
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public static GameState findByIdentifier(String identifier){
		Optional<GameState> optionalGameState = Lists.newArrayList(GameState.values())
				.stream().filter(x -> x.getIdentifier().equalsIgnoreCase(identifier)).findFirst();

		if(optionalGameState.isPresent()){
			return optionalGameState.get();
		}
		return null;
	}
}
