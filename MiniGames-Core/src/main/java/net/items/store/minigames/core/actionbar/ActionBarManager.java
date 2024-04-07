package net.items.store.minigames.core.actionbar;

import net.items.store.minigames.api.MiniGame;
import net.items.store.minigames.api.actionbar.IActionBarManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarManager implements IActionBarManager {

	@Override
	public void sendActionBarToPlayer(Player player, String message){
		Bukkit.getScheduler().callSyncMethod(MiniGame.getJavaPlugin(), () ->{
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
			return null;
		});
	}

	@Override
	public void sendActionBarToAll(String message){
		if(Bukkit.getOnlinePlayers().size() == 0){
			return;
		}
		for(Player player : Bukkit.getOnlinePlayers()){
			sendActionBarToPlayer(player, message);
		}
	}

}
