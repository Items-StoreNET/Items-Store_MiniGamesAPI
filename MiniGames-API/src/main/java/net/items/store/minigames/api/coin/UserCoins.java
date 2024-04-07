package net.items.store.minigames.api.coin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserCoins {

    private UUID uniqueID;
    private long coinAmount;

}
