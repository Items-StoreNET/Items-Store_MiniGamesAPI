package net.items.store.minigames.api.message;

import java.util.Map;

public interface IMessageManager {

    /**
     * @return The prefix, if it exists, otherwise null is returned.
     */
    String getPrefix();

    /**
     * Adds a new Message with the given identifier and the given message to the messageMap.
     * Also it adds the message to the config if its missing.
     * @param identifier
     * @param message
     */
    void addMessage(String identifier, String message);

    /**
     * Searches with the given identifier for a Message and return it.
     * @param identifier
     * @return The Message with the given identifier, if it exists, otherwise null is returned
     */
    String getMessage(String identifier);

    /**
     * Searches with the given identifier for a Message and return it.
     * Also it replaces data with the given objectObjectMap.
     * @param identifier
     * @param objectObjectMap
     * @return The Message with the given identifier, if it exists, otherwise null is returned
     */
    String getMessage(String identifier, Map<Object, Object> objectObjectMap);

    /**
     * Searches with the given identifier for a Message and return it.
     * @param identifier
     * @return The Message with the given identifier, if it exists, otherwise null is returned
     */
    String getMessageWithoutPrefix(String identifier);

    /**
     * Searches with the given identifier for a Message and return it.
     * Also it replaces data with the given objectObjectMap.
     * @param identifier
     * @param objectObjectMap
     * @return The Message with the given identifier, if it exists, otherwise null is returned
     */
    String getMessageWithoutPrefix(String identifier, Map<Object, Object> objectObjectMap);

}
