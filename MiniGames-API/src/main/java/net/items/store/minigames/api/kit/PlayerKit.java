package net.items.store.minigames.api.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class PlayerKit {

    private Kit kit;
    private String kitName;
    @Setter
    private boolean active;

}
