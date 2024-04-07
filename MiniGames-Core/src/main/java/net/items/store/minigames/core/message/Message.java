package net.items.store.minigames.core.message;

public class Message {

    private String identifier;
    private String defaultMessage;

    public Message(String identifier, String defaultMessage) {
        this.identifier = identifier;
        this.defaultMessage = defaultMessage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

}
