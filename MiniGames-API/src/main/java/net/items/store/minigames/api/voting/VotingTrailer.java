package net.items.store.minigames.api.voting;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.items.store.minigames.api.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class VotingTrailer {

    private VotingDetail votingDetail;
    private String identifier;
    private ItemBuilder itemBuilder;
    private int itemSlot;
    private final List<UUID> votes = Lists.newArrayList();

    public void addVote(UUID uuid){
        if(votes.contains(uuid) == false){
            votes.add(uuid);
        }
    }

    public void removeVote(UUID uuid){
        if(votes.contains(uuid) == true){
            votes.remove(uuid);
        }
    }

    public ItemStack getItem(){
        ItemBuilder itemBuilder = this.itemBuilder.copy();
        itemBuilder.setLore(Lists.newArrayList());
        itemBuilder.addLore("§7Votes§8: §a" + this.votes.size());
        return itemBuilder.buildItem();
    }

}
