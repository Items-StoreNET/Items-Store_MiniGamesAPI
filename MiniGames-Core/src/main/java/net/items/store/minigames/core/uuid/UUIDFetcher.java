package net.items.store.minigames.core.uuid;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class UUIDFetcher {

	private static Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();

	private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
	private static final String NAME_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s";

	private static Map<String, UUID> uuidCache = Maps.newHashMap();
	private static Map<UUID, String> nameCache = Maps.newHashMap();

	private String name;
	private UUID id;

	public static UUID getUUID(String name) {
		return getUUIDAt(name, System.currentTimeMillis());
	}

	public static UUID getUUIDAt(String name, long timestamp) {
		name = name.toLowerCase();
		if (uuidCache.containsKey(name)) {
			return uuidCache.get(name);
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name, timestamp / 1000)).openConnection();
			connection.setReadTimeout(5000);
			UUIDFetcher data = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);

			if (data == null) {
				connection.disconnect();
				return null;
			} else {
				uuidCache.put(name, data.id);
				nameCache.put(data.id, data.name);
				connection.disconnect();
				return data.id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getName(UUID uuid) {
		if (nameCache.containsKey(uuid)) {
			return nameCache.get(uuid);
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
			connection.setReadTimeout(5000);
			UserProfile userProfile = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UserProfile.class);

			uuidCache.put(userProfile.getName().toLowerCase(), uuid);
			nameCache.put(uuid, userProfile.getName());

			connection.disconnect();
			return userProfile.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}