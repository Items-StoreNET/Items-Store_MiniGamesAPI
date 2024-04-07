package net.items.store.minigames.core.kit.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.api.kit.PlayerKit;
import net.items.store.minigames.core.event.CountdownGameStateChangedEvent;
import net.items.store.minigames.core.kit.KitManager;
import net.items.store.minigames.core.kit.PlayerKitManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class KitGameStateChangedListener implements Listener {

    @EventHandler
    public void onCountdownGameStateChanged(CountdownGameStateChangedEvent countdownGameStateChangedEvent){
        if (countdownGameStateChangedEvent.getOldGameState() == GameState.LOBBY){
            try {
                PlayerKitManager playerKitManager = MiniGame.get(PlayerKitManager.class);
                IKitManager kitManager = MiniGame.get(KitManager.class);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerKit playerKit = playerKitManager.getPlayerKit(player);
                    Kit kit = playerKit.getKit();

                    kitManager.givePlayerKit(player, kit);
                }
            } catch (Exception exception){
                System.out.println(exception.toString());
            }
        }
    }
}
