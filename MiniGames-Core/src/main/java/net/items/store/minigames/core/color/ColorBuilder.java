package net.items.store.minigames.core.color;

import org.bukkit.Color;

public class ColorBuilder {
	
	public static Color getColorFromString(String name) {
		switch (name.toUpperCase()) {
			case "AQUA":
				return Color.AQUA;
			case "BLACK":
				return Color.BLACK;
			case "BLUE":
				return Color.BLUE;
			case "FUCHSIA":
				return Color.FUCHSIA;
			case "GREEN":
				return Color.GREEN;
			case "LIME":
				return Color.LIME;
			case "MAROON":
				return Color.MAROON;
			case "NAVY":
				return Color.NAVY;
			case "OLIVE":
				return Color.OLIVE;
			case "ORANGE":
				return Color.ORANGE;
			case "PURPLE":
				return Color.PURPLE;
			case "RED":
				return Color.RED;
			case "SILVER":
				return Color.SILVER;
			case "TEAL":
				return Color.TEAL;
			case "WHITE":
				return Color.WHITE;
			case "YELLOW":
				return Color.YELLOW;
			default:
				return Color.GRAY;
		}
	}

}
