package net.items.store.minigames.api.sql;

import lombok.Getter;

@Getter
public enum SQLDataType {

    DOUBLE("double", 0.0),
    INT("int", 0),
    BOOLEAN("boolean", false),
    LONG("long", 0),
    VARCHAR("varchar", ""),
    TEXT("text", "");

    private String sqlName;
    private Object defaultValue;

    SQLDataType(String sqlName, Object defaultValue){
        this.sqlName = sqlName;
        this.defaultValue = defaultValue;
    }
}
