package net.items.store.minigames.core.data;

import com.google.common.collect.Lists;
import net.items.store.minigames.api.item.ItemBuilder;
import net.items.store.minigames.core.color.ColorBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;

public class DataBuilder {
	
	public static void checkPathAndCreateIfNotExists(String filePath, String fullPathWithFile) throws IOException {
		if(Files.notExists(Paths.get(filePath))) {
			Files.createDirectory(Paths.get(filePath));
		}
		if(!Files.exists(Paths.get(fullPathWithFile), LinkOption.NOFOLLOW_LINKS)) {
			Files.createFile(Paths.get(fullPathWithFile));
		}
	}

	public static ItemBuilder getItemBuilderFromJSONObject(JSONObject jsonObject){
		Material material = null;
		int amount = 1;
		String displayName = "";
		PotionEffectType potionEffectType = null;
		int potionEffectDuration = 0;
		int potionEffectAmblifier = 0;
		JSONArray loreJSONArray = null;
		JSONArray enchantmentJSONArray = null;
		Color color = null;
		Enchantment enchantment;
		JSONObject enchantmentJSONObject;
		Integer enchantmentLevel;

		if(jsonObject.containsKey("material")){
			material = Material.valueOf(jsonObject.get("material").toString());
		}
		if (jsonObject.containsKey("potionEffectType")){
			potionEffectType = PotionEffectType.getByName(jsonObject.get("potionEffectType").toString());
		}
		if (jsonObject.containsKey("potionEffectDuration")){
			potionEffectDuration = Integer.valueOf(jsonObject.get("potionEffectDuration").toString());
		}
		if (jsonObject.containsKey("potionEffectAmblifier")){
			potionEffectAmblifier = Integer.valueOf(jsonObject.get("potionEffectAmblifier").toString());
		}
		if(jsonObject.containsKey("amount")){
			amount = Integer.valueOf(jsonObject.get("amount").toString());
		}
		if(jsonObject.containsKey("displayName")){
			displayName = jsonObject.get("displayName").toString();
		}
		if(jsonObject.containsKey("color")){
			color = ColorBuilder.getColorFromString(jsonObject.get("color").toString());
		}
		if(jsonObject.containsKey("lore")){
			loreJSONArray = (JSONArray) jsonObject.get("lore");
		}
		if(jsonObject.containsKey("enchantments")) {
			enchantmentJSONArray = (JSONArray) jsonObject.get("enchantments");
		}
		ItemBuilder itemBuilder = ItemBuilder.modify()
				.setMaterial(material)
				.setAmount(amount)
				.setDisplayName(displayName)
				.setColor(color)
				.setPotionEffectType(potionEffectType)
				.setPotionDuration(potionEffectDuration)
				.setPotionAmplifier(potionEffectAmblifier);

		if(loreJSONArray != null){
			for(int i = 0; i < loreJSONArray.size(); i++){
				itemBuilder.addLore(loreJSONArray.get(i).toString());
			}
		}
		if(enchantmentJSONArray != null){
			for(int i = 0; i < enchantmentJSONArray.size(); i++){
				enchantmentJSONObject = (JSONObject) enchantmentJSONArray.get(i);

				enchantment = Enchantment.getByName(enchantmentJSONObject.get("enchantmentName").toString());
				enchantmentLevel = Integer.valueOf(enchantmentJSONObject.get("enchantmentLevel").toString());

				if(enchantment != null){
					itemBuilder.addEnchant(enchantment, enchantmentLevel);
				}
			}
		}
		return itemBuilder;
	}

	public static String getTime(long integer){
		String time = "00:00";
		long seconds = integer;
		Integer minutes = 0;

		while(seconds >= 60){
			seconds -= 60;
			minutes++;
		}

		if(minutes > 9 && seconds > 9){
			time = minutes + ":" + seconds;
		} else if(minutes > 9 && seconds < 10){
			time = minutes + ":0" + seconds;
		} else if(minutes < 10 && seconds > 9){
			time = "0" + minutes + ":" + seconds;
		} else if(minutes < 10 && seconds < 10){
			time = "0" + minutes + ":0" + seconds;
		}
		return time;
	}
	
	public static String getTimeWithHours(long integer){
		long seconds = integer;
		Integer hours = 0;
		Integer minutes = 0;
		
		while(seconds >= 60){
			seconds -= 60;
			minutes++;
		}
		while(minutes >= 60) {
			minutes -= 60;
			hours++;
		}

		if(hours > 9 && minutes > 9 && seconds > 9) {
			return hours + ":" + minutes + ":" + seconds;
		} else if(hours < 10 && minutes < 10 && seconds < 10) {
			return "0" + hours + ":0" + minutes + ":0" + seconds;
		} else if(hours > 9 && minutes < 10 && seconds < 10) {
			return hours + ":0" + minutes + ":0" + seconds;
		} else if(hours < 10 && minutes > 9 && seconds < 10) {
			return "0" + hours + ":" + minutes + ":0" + seconds;
		} else if(hours < 10 && minutes < 10 && seconds > 9) {
			return "0" + hours + ":0" + minutes + ":" + seconds;
		} else if(hours < 10 && minutes > 9 && seconds > 9) {
			return "0" + hours + ":" + minutes + ":" + seconds;
		} else if(hours > 9 && minutes > 9 && seconds < 10) {
			return hours + ":" + minutes + ":0" + seconds;
		} else if(hours > 9 && minutes < 10 && seconds > 9) {
			return hours + ":0" + minutes + ":" + seconds;
		} else {
			return "00:00";
		}
	}
	
	public static List<String> replaceLore(List<String> currentLore, String oldString, String newString) {
		List<String> newLore = Lists.newArrayList();
		
		for(String string : currentLore) {
			newLore.add(string.replace(oldString, newString));
		}
		return newLore;
	}

}
