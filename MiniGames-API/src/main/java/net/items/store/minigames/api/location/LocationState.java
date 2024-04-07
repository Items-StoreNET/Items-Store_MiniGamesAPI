package net.items.store.minigames.api.location;

import java.util.Map;

public enum LocationState {

    LOBBY(".lobby"),
    NORMAL(".normal.{MAP}"),
    TEAM(".team.{MAP}.{TEAMNAME}"),
    COUNT(".{MAP}");

    private String identifier;

    LocationState(String identifier){
        this.identifier = identifier;
    }

    public String getIdentifier(String replaceKey, String replaceValue) {
        return identifier.replace(replaceKey, replaceValue);
    }

    public String getIdentifier(Map<Object, Object> objectObjectMap) {
        String copyIdentifier = identifier;

        for(Object object : objectObjectMap.keySet()){
            copyIdentifier = copyIdentifier.replace(object.toString(), objectObjectMap.get(object).toString());
        }
        return copyIdentifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
