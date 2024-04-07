package net.items.store.minigames.api.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
public class GameLocation {

    private String identifier;
    private String worldName;
    @Setter
    private Location location;

}
