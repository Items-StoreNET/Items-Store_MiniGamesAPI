package net.items.store.minigames.core.kit.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.IPlayerKitManager;
import net.items.store.minigames.core.kit.KitManager;
import net.items.store.minigames.core.kit.PlayerKitManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        IPlayerKitManager playerKitManager = MiniGame.get(PlayerKitManager.class);
        playerKitManager.createUser(playerJoinEvent.getPlayer());

        if(MiniGame.get(AbstractGame.class).getGameState() == GameState.LOBBY){
            IKitManager kitManager = MiniGame.get(KitManager.class);
            kitManager.setItemToInventory(playerJoinEvent.getPlayer());
        }
    }
}
