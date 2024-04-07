package net.items.store.minigames.core.voting.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.voting.IVotingManager;
import net.items.store.minigames.api.voting.VotingHeader;
import net.items.store.minigames.core.voting.VotingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        IVotingManager votingManager = MiniGame.get(VotingManager.class);

        for (VotingHeader votingHeader : votingManager.getVotingHeaders()){
            if(playerInteractEvent.getItem() != null && playerInteractEvent.getItem().getItemMeta() != null
                    && playerInteractEvent.getItem().getItemMeta().getDisplayName() != null){
                if(votingHeader.compareItem(playerInteractEvent.getItem())){
                    votingManager.openHeaderInventory(playerInteractEvent.getPlayer(), votingHeader.getVotingIdentifier());
                    break;
                }
            }
        }
    }
}
