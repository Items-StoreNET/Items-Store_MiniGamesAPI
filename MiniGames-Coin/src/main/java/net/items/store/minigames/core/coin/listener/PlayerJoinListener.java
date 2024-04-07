package net.items.store.minigames.core.coin.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.coin.ICoinManager;
import net.items.store.minigames.core.coin.CoinManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        ICoinManager coinManager = MiniGame.get(CoinManager.class);
        coinManager.createUser(playerJoinEvent.getPlayer());
    }
}
