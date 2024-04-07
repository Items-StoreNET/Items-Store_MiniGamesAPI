package net.items.store.minigames.core.listener.entity;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.game.AbstractGame;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.player.IPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent entityDamageEvent){
        IPlayerManager playerManager = MiniGame.get(IPlayerManager.class);
        AbstractGame abstractGame = MiniGame.get(AbstractGame.class);

        if(abstractGame.getGameInteraction().isDamageAble() == false ||
                playerManager.isPlayerSpectator((Player) entityDamageEvent.getEntity()) == true){
            entityDamageEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent){
        IPlayerManager playerManager = MiniGame.get(IPlayerManager.class);
        AbstractGame abstractGame = MiniGame.get(AbstractGame.class);

        if(entityDamageByEntityEvent.getDamager() != null
                && entityDamageByEntityEvent.getDamager() instanceof Player
                && playerManager.canPlayerBuild(entityDamageByEntityEvent.getDamager().getUniqueId()) == false) {
            if (playerManager.isPlayerSpectator((Player) entityDamageByEntityEvent.getDamager()) ||
                    playerManager.isPlayerSpectator((Player) entityDamageByEntityEvent.getEntity())) {
                entityDamageByEntityEvent.setCancelled(true);
                return;
            }
            if (entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                Projectile projectile = (Projectile) entityDamageByEntityEvent.getEntity();

                if (projectile.getShooter() instanceof Player){
                    Player player = (Player) projectile.getShooter();

                    if (playerManager.isPlayerSpectator(player)){
                        entityDamageByEntityEvent.setCancelled(true);
                        return;
                    }
                }
            }
            if (abstractGame.getGameInteraction().isDamageAble() == false) {
                entityDamageByEntityEvent.setCancelled(true);
            }
        }
    }
}
