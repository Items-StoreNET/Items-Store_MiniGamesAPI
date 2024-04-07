package net.items.store.minigames.api.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@Getter
public class GameMap {
	
	private String mapName;
	@Setter
	private Map<String, List<Location>> locationList;
	@Setter
	private Material mapMaterial;
	private String worldName;

	public GameMap(String mapName, Material mapMaterial, String worldName){
		this.mapName = mapName;
		this.locationList = Maps.newHashMap();
		this.mapMaterial = mapMaterial;
		this.worldName = worldName;
	}

	public void addLocation(String identifier, Location location){
		this.locationList.put(identifier, Lists.newArrayList(location));
	}

	public void addLocation(String identifier, List<Location> locations){
		this.locationList.put(identifier, locations);
	}
}
