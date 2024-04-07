package net.items.store.minigames.api.voting;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.items.store.minigames.api.item.ItemHelper;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class VotingHeader {

    private String votingIdentifier;
    private String inventoryName;
    private int inventorySize;
    private ItemStack playerItem;
    private int playerItemSlot;
    private final List<VotingDetail> votingDetailList = Lists.newArrayList();

    public VotingDetail getVotingDetail(String detailIdentifier){
        VotingDetail votingDetail = null;

        for(VotingDetail currentVotingDetail : votingDetailList){
            if(currentVotingDetail.getIdentifier().equalsIgnoreCase(detailIdentifier)){
                votingDetail = currentVotingDetail;
                break;
            }
        }
        return votingDetail;
    }

    public VotingDetail getVotingDetail(UUID uuid){
        VotingDetail votingDetail = null;

        for(VotingDetail currentVotingDetail : votingDetailList){
            if(currentVotingDetail.getVotingTrailer(uuid) != null){
                votingDetail = currentVotingDetail;
                break;
            }
        }
        return votingDetail;
    }

    public VotingDetail getVotingDetail(ItemStack clickedItem, int clickedSlot){
        VotingDetail votingDetail = null;

        for(VotingDetail currentVotingDetail : votingDetailList){
            if(ItemHelper.compareItems(currentVotingDetail.getInventoryItem(), clickedItem)
                    && currentVotingDetail.getInventorySlot() == clickedSlot){
                votingDetail = currentVotingDetail;
                break;
            }
        }
        return votingDetail;
    }

    public VotingTrailer getVotingTrailer(ItemStack clickedItem){
        VotingTrailer votingTrailer = null;

        for(VotingDetail votingDetail : votingDetailList){
            for(VotingTrailer currentVotingTrailer : votingDetail.getVotingTrailerList()){
                if(ItemHelper.compareItems(currentVotingTrailer.getItem(), clickedItem)){
                    votingTrailer = currentVotingTrailer;
                    break;
                }
            }

            if (votingTrailer != null){
                break;
            }
        }

        return votingTrailer;
    }

    public VotingTrailer getVotingTrailer(ItemStack clickedItem, int clickedSlot){
        VotingTrailer votingTrailer = null;

        for(VotingDetail votingDetail : votingDetailList){
            for(VotingTrailer currentVotingTrailer : votingDetail.getVotingTrailerList()){
                if(ItemHelper.compareItems(currentVotingTrailer.getItem(), clickedItem)
                        && currentVotingTrailer.getItemSlot() == clickedSlot){
                    votingTrailer = currentVotingTrailer;
                    break;
                }
            }

            if (votingTrailer != null){
                break;
            }
        }

        return votingTrailer;
    }

    public boolean compareItem(ItemStack itemStack){
        return itemStack.getType() == playerItem.getType()
                && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(playerItem.getItemMeta().getDisplayName());
    }

}
