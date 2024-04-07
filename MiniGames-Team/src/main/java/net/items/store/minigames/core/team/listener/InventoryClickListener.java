package net.items.store.minigames.core.team.listener;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.api.team.GameTeam;
import net.items.store.minigames.api.team.ITeamManager;
import net.items.store.minigames.core.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player) inventoryClickEvent.getWhoClicked();

        if (inventoryClickEvent.getClickedInventory() != null
                && player.getOpenInventory() != null) {
            ITeamManager teamManager = MiniGame.get(TeamManager.class);

            if (player.getOpenInventory().getTitle()
                    .equalsIgnoreCase(teamManager.getInventoryName())) {
                inventoryClickEvent.setCancelled(true);

                if (inventoryClickEvent.getCurrentItem() != null && inventoryClickEvent.getCurrentItem().getItemMeta() != null
                        && inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    GameTeam gameTeam = teamManager.getTeamFromItemStack(inventoryClickEvent.getCurrentItem());

                    if (gameTeam != null) {
                        teamManager.joinTeam(player, gameTeam.getTeamName());
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
