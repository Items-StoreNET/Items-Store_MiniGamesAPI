package net.items.store.minigames.api.message;

import java.util.Map;

public interface IMessageJsonManager {

    /**
     * Loads all messages from the json file and creates the json file if it not exists
     * @return a Map with all messages and it keys.
     */
    Map<String, String> loadMessages();

    /**
     * Adds a new message to the json file if it not exists.
     * @param identifier
     * @param message
     */
    void addMessageToJson(String identifier, String message);

}
