package net.items.store.minigames.core.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.player.IPlayerManager;
import net.items.store.minigames.core.game.Game;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerManager implements IPlayerManager {

    private static final List<UUID> BUILD_LIST = Lists.newArrayList();

    @Override
    public boolean canPlayerBuild(UUID uniqueID) {
        return BUILD_LIST.contains(uniqueID);
    }

    @Override
    public void setPlayerCanBuild(UUID uniqueID, boolean build) {
        if (build == true && BUILD_LIST.contains(uniqueID) == false){
            BUILD_LIST.add(uniqueID);
            return;
        }
        if (build == false && BUILD_LIST.contains(uniqueID) == true){
            BUILD_LIST.remove(uniqueID);
            return;
        }
    }

    @Override
    public boolean isPlayerSpectator(Player player) {
        Game game = MiniGame.get(Game.class);

        return game.isPlayerAlive(player) == false;
    }
}
