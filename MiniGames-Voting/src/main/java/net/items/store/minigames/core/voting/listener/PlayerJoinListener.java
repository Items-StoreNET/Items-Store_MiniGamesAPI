package net.items.store.minigames.core.voting.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.voting.IVotingManager;
import net.items.store.minigames.api.voting.VotingHeader;
import net.items.store.minigames.core.voting.VotingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        if(MiniGame.get(AbstractGame.class).getGameState() == GameState.LOBBY){
            IVotingManager votingManager = MiniGame.get(VotingManager.class);

            for (VotingHeader votingHeader : votingManager.getVotingHeaders()){
                playerJoinEvent.getPlayer().getInventory().setItem(votingHeader.getPlayerItemSlot(), votingHeader.getPlayerItem());
            }
        }
    }
}
