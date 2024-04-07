package net.items.store.minigames.core.team.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.team.TeamManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        if(MiniGame.get(AbstractGame.class).getGameState() == GameState.LOBBY) {
            ITeamManager teamManager = MiniGame.get(TeamManager.class);
            teamManager.setItemToInventory(playerJoinEvent.getPlayer());
        }
    }
}
