package net.items.store.minigames.api.game;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.items.store.minigames.api.IDefaultManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class AbstractGame implements IDefaultManager {

	@Getter
	protected GameState gameState;
	@Getter
	private final List<GameInteraction> gameInteractions;
	private final List<UUID> alivePlayerList;
	@Setter
	@Getter
	private int minPlayers;
	@Setter
	@Getter
	private int maxPlayers;
	
	public AbstractGame() {
		loadMaps();

		this.gameInteractions = Lists.newArrayList();
		this.alivePlayerList = Lists.newArrayList();
		this.gameState = GameState.LOBBY;

		loadDefaultGameInteractions();
	}

	public void addPlayerAlive(Player player){
		if (alivePlayerList.contains(player.getUniqueId()) == false){
			alivePlayerList.add(player.getUniqueId());
		}
	}

	public void removePlayerAlive(Player player){
		if (alivePlayerList.contains(player.getUniqueId()) == true){
			alivePlayerList.remove(player.getUniqueId());
		}
	}

	public boolean isPlayerAlive(Player player){
		return alivePlayerList.contains(player.getUniqueId());
	}

	public List<UUID> getAlivePlayers(){
		return alivePlayerList;
	}

	public List<UUID> containsAlivePlayers(List<UUID> uuids){
		List<UUID> containsPlayers = Lists.newArrayList();

		for (UUID uuid : uuids){
			if (this.alivePlayerList.contains(uuid)){
				containsPlayers.add(uuid);
			}
		}

		return containsPlayers;
	}

	public GameInteraction getGameInteraction(){
		return this.gameInteractions.stream()
				.filter(x -> x.getGameState() == gameState).findFirst()
				.orElse(new GameInteraction(GameState.LOBBY, false, false, false, false, false));
	}

	public GameInteraction getGameInteraction(GameState gameState){
		return this.gameInteractions.stream()
				.filter(x -> x.getGameState() == gameState).findFirst()
				.orElse(new GameInteraction(GameState.LOBBY, false, false, false, false, false));
	}

	public void setGameInteraction(GameState gameState, GameInteraction gameInteraction){
		this.gameInteractions.remove(getGameInteraction(gameState));
		this.gameInteractions.add(gameInteraction);
	}

	private void loadDefaultGameInteractions(){
		this.gameInteractions.add(new GameInteraction(GameState.LOBBY, false, false, false, false,false));
		this.gameInteractions.add(new GameInteraction(GameState.WAITING, true, true, true, true,false));
		this.gameInteractions.add(new GameInteraction(GameState.INGAME, true, true, true, true,true));
		this.gameInteractions.add(new GameInteraction(GameState.SHOP, false, false, false, false,false));
		this.gameInteractions.add(new GameInteraction(GameState.DEATHMATCH, true, false, true, true,true));
		this.gameInteractions.add(new GameInteraction(GameState.RESTART, false, false, false, false,false));
	}

	public abstract void setGameState(GameState gameState, GameStateChangeReason reason);
	protected abstract void loadMaps();

}
