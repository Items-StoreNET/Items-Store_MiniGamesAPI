package net.items.store.minigames.core.message;

import net.items.store.minigames.api.message.IMessageManager;
import net.items.store.minigames.api.message.IMessageJsonManager;

import java.util.Map;

public class MessageManager implements IMessageManager {

    private Map<String, String> messages;
    private IMessageJsonManager messageJsonManager;

    public MessageManager() {
        this.messageJsonManager = new MessageJsonManager();
        this.messages = this.messageJsonManager.loadMessages();
    }

    @Override
    public String getPrefix() {
        if(this.messages.containsKey("Prefix")) {
            return this.messages.get("Prefix");
        }
        return "NULL";
    }

    @Override
    public void addMessage(String identifier, String message) {
        if(!this.messages.containsKey(identifier)) {
            this.messages.put(identifier, message);
            this.messageJsonManager.addMessageToJson(identifier, message);
        }
    }

    @Override
    public String getMessage(String identifier) {
        String formatMessage = getMessageWithoutPrefix(identifier);
        formatMessage = formatMessage.replace("{PREFIX}", getPrefix());
        return formatMessage;
    }

    @Override
    public String getMessage(String identifier, Map<Object, Object> objectObjectMap) {
        String formatMessage = getMessage(identifier);

        for(Object object : objectObjectMap.keySet()){
            formatMessage = formatMessage.replace(object.toString(), objectObjectMap.get(object).toString());
        }
        return formatMessage;
    }

    @Override
    public String getMessageWithoutPrefix(String identifier) {
        if(this.messages.containsKey(identifier)) {
            String message = this.messages.get(identifier);
            return message;
        } else {
            return "404 not found.";
        }
    }

    @Override
    public String getMessageWithoutPrefix(String identifier, Map<Object, Object> objectObjectMap) {
        String formatMessage = getMessageWithoutPrefix(identifier);

        for(Object object : objectObjectMap.keySet()){
            formatMessage = formatMessage.replace(object.toString(), objectObjectMap.get(object).toString());
        }
        return formatMessage;
    }
}
