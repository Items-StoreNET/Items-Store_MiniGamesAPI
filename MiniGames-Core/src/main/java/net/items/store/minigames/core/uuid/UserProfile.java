package net.items.store.minigames.core.uuid;

import java.util.UUID;

public class UserProfile {

    private UUID id;
    private String name;
    public Properties[] properties;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Properties[] getProperties() {
        return properties;
    }

    private class Properties
    {
        private String name;
        private String value;

        public String getName(){
            return name;
        }

        public String getValue(){
            return value;
        }
    }
}
