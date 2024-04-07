package net.items.store.minigames.api.player;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface IPlayerManager {

    boolean canPlayerBuild(UUID uniqueID);

    void setPlayerCanBuild(UUID uniqueID, boolean build);

    boolean isPlayerSpectator(Player player);

}
