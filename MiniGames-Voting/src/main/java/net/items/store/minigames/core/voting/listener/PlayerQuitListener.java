package net.items.store.minigames.core.voting.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.voting.IVotingManager;
import net.items.store.minigames.api.voting.VotingDetail;
import net.items.store.minigames.api.voting.VotingHeader;
import net.items.store.minigames.core.voting.VotingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){
        IVotingManager votingManager = MiniGame.get(VotingManager.class);

        for (VotingHeader votingHeader : votingManager.getVotingHeaders()){
            for (VotingDetail votingDetail : votingHeader.getVotingDetailList()){
                votingDetail.removePlayerFromVoting(playerQuitEvent.getPlayer().getUniqueId());
            }
        }
    }
}
