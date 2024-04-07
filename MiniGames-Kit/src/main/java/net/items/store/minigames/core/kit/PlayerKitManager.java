package net.items.store.minigames.core.kit;

import com.google.common.collect.Maps;
import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.coin.ICoinManager;
import net.items.store.minigames.api.game.GameScoreboard;
import net.items.store.minigames.api.game.GameState;
import net.items.store.minigames.api.inventory.IInventoryManager;
import net.items.store.minigames.api.kit.IKitManager;
import net.items.store.minigames.api.kit.IPlayerKitManager;
import net.items.store.minigames.api.kit.Kit;
import net.items.store.minigames.api.kit.PlayerKit;
import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.scoreboard.IScoreboardManager;
import net.items.store.minigames.api.sql.IMySQL;
import net.items.store.minigames.core.inventory.InventoryManager;
import net.items.store.minigames.core.message.MessageManager;
import net.items.store.minigames.core.scoreboard.DefaultScoreboardManager;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerKitManager implements IPlayerKitManager {

    private Map<UUID, List<PlayerKit>> playerKitMap;
    private PlayerKitTable playerKitTable;

    public PlayerKitManager(){
        this.playerKitMap = Maps.newHashMap();
        this.playerKitTable = new PlayerKitTable(MiniGame.get(IMySQL.class));
    }

    @Override
    public void createUser(Player player) {
        this.playerKitTable.createPlayer(player.getUniqueId());
    }

    @Override
    public boolean hasKit(Player player, Kit kit) {
        List<PlayerKit> playerKitList = getPlayerKits(player);
        Optional<PlayerKit> optionalPlayerKit = playerKitList.stream()
                .filter(x -> x.getKitName().equalsIgnoreCase(kit.getKitName())).findFirst();

        if(optionalPlayerKit.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public boolean buyKit(Player player, Kit kit) {
        IMySQL mySQL = MiniGame.get(IMySQL.class);
        if(mySQL.isMySQLConnected()) {
            ICoinManager coinManager = MiniGame.get(ICoinManager.class);

            if (coinManager != null) {
                IMessageManager messageManager = MiniGame.get(MessageManager.class);

                if (coinManager.getCoins(player.getUniqueId()) >= kit.getKitPrice()) {
                    coinManager.removeCoins(player, kit.getKitPrice());

                    PlayerKit playerKit = new PlayerKit(kit, kit.getKitName(), false);
                    List<PlayerKit> playerKitList = getPlayerKits(player);
                    playerKitList.add(playerKit);

                    this.playerKitMap.put(player.getUniqueId(), playerKitList);
                    this.playerKitTable.savePlayerKits(player.getUniqueId(), playerKitList);

                    Map<Object, Object> objectObjectMap = Maps.newHashMap();
                    objectObjectMap.put("{KIT}", kit.getKitName());

                    player.sendMessage(messageManager.getMessage("KitPlayerBuyed", objectObjectMap));
                } else {
                    player.sendMessage(messageManager.getMessage("NotEnoughCoins"));
                }
            }
        }
        return false;
    }

    @Override
    public PlayerKit getPlayerKit(Player player) {
        List<PlayerKit> playerKitList = getPlayerKits(player);
        Optional<PlayerKit> optionalPlayerKit = playerKitList.stream().filter(x -> x.isActive()).findAny();

        if(optionalPlayerKit.isPresent()){
            return optionalPlayerKit.get();
        }
        if (playerKitList.size() > 0){
            return playerKitList.get(0);
        }
        return null;
    }

    @Override
    public List<PlayerKit> getPlayerKits(Player player) {
        if (this.playerKitMap.containsKey(player.getUniqueId())) {
            return this.playerKitMap.get(player.getUniqueId());
        }

        List<PlayerKit> playerKitList = this.playerKitTable.getPlayerKits(player.getUniqueId());
        this.playerKitMap.put(player.getUniqueId(), playerKitList);

        return playerKitList;
    }

    @Override
    public void takeRandomKit(Player player) {
        PlayerKit playerKit = getPlayerKit(player);
        if(playerKit != null){
            playerKit.setActive(false);
        }
        List<PlayerKit> playerKitList = getPlayerKits(player);
        Random random = new Random();
        int next = random.nextInt(playerKitList.size());
        PlayerKit nextPlayerKit = playerKitList.get(next);
        nextPlayerKit.setActive(true);
    }

    @Override
    public void useKit(Player player, Kit kit) {
        if(hasKit(player, kit)){
            PlayerKit currentPlayerKit = getPlayerKit(player);
            if(currentPlayerKit != null && currentPlayerKit.getKitName() == kit.getKitName()){
                return;
            }
            if(currentPlayerKit != null) {
                currentPlayerKit.setActive(false);
            }

            List<PlayerKit> playerKitList = getPlayerKits(player);
            for(PlayerKit playerKit : playerKitList){
                if (playerKit.getKitName().equalsIgnoreCase(kit.getKitName())) {
                    playerKit.setActive(true);

                    this.playerKitTable.savePlayerKits(player.getUniqueId(), playerKitList);

                    Map<Object, Object> objectObjectMap = Maps.newHashMap();
                    objectObjectMap.put("{KIT}", playerKit.getKitName());

                    IMessageManager messageManager = MiniGame.get(MessageManager.class);
                    player.sendMessage(messageManager.getMessage("KitPlayerUsed", objectObjectMap));
                    break;
                }
            }

            IScoreboardManager scoreboardManager = MiniGame.get(DefaultScoreboardManager.class);
            GameScoreboard gameScoreboard = scoreboardManager.findScoreboardByIdentifier(GameState.LOBBY);

            if (gameScoreboard != null){
                scoreboardManager.updateScoreboardForPlayer(gameScoreboard, player);
            }
        } else {
            IInventoryManager inventoryManager = MiniGame.get(InventoryManager.class);
            inventoryManager.openInventory(player, KitInventoryIdentifier.KIT_IDENTIFIER_KIT_BUY, kit);
        }
    }

    @Override
    public void registerDefault() {
        IMessageManager messageManager = MiniGame.get(MessageManager.class);
        messageManager.addMessage("KitPlayerUsed", "{PREFIX}§7Du hast das Kit §e{KIT} §aausgewählt§8.");
        messageManager.addMessage("KitPlayerBuyed", "{PREFIX}§7Du hast das Kit §e{KIT} §agekauft§8!");
        messageManager.addMessage("NotEnoughCoins", "{PREFIX}§cDu hast nicht genügend Coins um dir dieses Kit zu kaufen.");
    }
}
