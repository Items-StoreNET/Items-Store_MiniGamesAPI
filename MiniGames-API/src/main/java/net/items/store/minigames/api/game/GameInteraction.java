package net.items.store.minigames.api.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GameInteraction {

    private GameState gameState;
    private boolean interactAble;
    private boolean buildAble;
    private boolean dropAble;
    private boolean pickupAble;
    private boolean damageAble;

}
