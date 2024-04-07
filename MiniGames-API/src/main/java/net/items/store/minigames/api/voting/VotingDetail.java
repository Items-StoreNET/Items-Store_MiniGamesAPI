package net.items.store.minigames.api.voting;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.items.store.minigames.api.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class VotingDetail {

    private VotingHeader votingHeader;
    private String identifier;
    private String nameWithPrefix;
    private ItemStack inventoryItem;
    private int inventorySlot;
    private final List<VotingTrailer> votingTrailerList = Lists.newArrayList();

    public int getVotings(String identifier){
        VotingTrailer votingTrailer = getVotingTrailer(identifier);

        return votingTrailer != null ? votingTrailer.getVotes().size() : 0;
    }

    public boolean playerAlreadyInVoting(String identifier, UUID uuid){
        VotingTrailer votingTrailer = getVotingTrailer(identifier);

        return votingTrailer != null && votingTrailer.getVotes().contains(uuid);
    }

    public void addToVotingTrailer(String identifier, ItemBuilder itemBuilder, int itemSlot){
        VotingTrailer votingTrailer = new VotingTrailer(this, identifier, itemBuilder, itemSlot);

        this.votingTrailerList.add(votingTrailer);
    }

    public boolean addPlayerToVoting(VotingTrailer votingTrailer, UUID uuid){
        if(votingTrailer != null && votingTrailer.getVotes().contains(uuid) == false){
            removePlayerFromVoting(uuid);
            votingTrailer.addVote(uuid);
            return true;
        }
        return false;
    }

    public void removePlayerFromVoting(UUID uuid){
        for(VotingTrailer votingTrailer : votingTrailerList){
            if(votingTrailer.getVotes().contains(uuid)){
                votingTrailer.removeVote(uuid);
            }
        }
    }

    public VotingTrailer getVotingWinner(){
        VotingTrailer votingTrailer = null;
        int votes = 0;

        if (votingTrailerList.size() > 0){
            votingTrailer = votingTrailerList.get(0);
            votes = votingTrailerList.get(0).getVotes().size();
        }

        for(VotingTrailer currentVotingTrailer : votingTrailerList){
            if(currentVotingTrailer.getVotes().size() > votes){
                votingTrailer = currentVotingTrailer;
            }
        }
        return votingTrailer;
    }

    public VotingTrailer getVotingTrailer(String identifier){
        VotingTrailer votingTrailer = null;

        for(VotingTrailer currentVotingTrailer : votingTrailerList){
            if(currentVotingTrailer.getIdentifier().equalsIgnoreCase(identifier)){
                votingTrailer = currentVotingTrailer;
                break;
            }
        }
        return votingTrailer;
    }

    public VotingTrailer getVotingTrailer(UUID uuid){
        VotingTrailer votingTrailer = null;

        for(VotingTrailer currentVotingTrailer : votingTrailerList){
            if(currentVotingTrailer.getVotes().contains(uuid)){
                votingTrailer = currentVotingTrailer;
                break;
            }
        }
        return votingTrailer;
    }

}
