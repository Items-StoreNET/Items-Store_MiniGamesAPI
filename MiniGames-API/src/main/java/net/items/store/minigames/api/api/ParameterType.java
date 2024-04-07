package net.items.store.minigames.api.api;

import lombok.Getter;

@Getter
public enum ParameterType {

    INTEGER(Integer.class),
    STRING(String.class),
    LONG(Long.class),
    DOUBLE(Double.class);

    private Class objectClass;

    ParameterType(Class objectClass){
        this.objectClass = objectClass;
    }

    public static ParameterType get(Class objectClass){
        ParameterType parameterType = null;

        for (ParameterType currentType : values()){
            if (currentType.getObjectClass().getName().equals(objectClass.getName())) {
                parameterType = currentType;
                break;
            }
        }

        if (parameterType == null){
            System.out.println("[ParameterType] Nothing found for " + objectClass.getName());
        }

        return parameterType;
    }
}
