package net.items.store.minigames.api.sql;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

@Getter
public class SQLDataColumn {

    private String name;
    private SQLDataType sqlDataType;
    private int length;
    private List<SQLDataSetting> sqlDataSettings;

    public SQLDataColumn(String name, SQLDataType sqlDataType){
        this(name, sqlDataType, 0, SQLDataSetting.CAN_BE_NULL);
    }

    public SQLDataColumn(String name, SQLDataType sqlDataType, int length){
        this(name, sqlDataType, length, SQLDataSetting.CAN_BE_NULL);
    }

    public SQLDataColumn(String name, SQLDataType sqlDataType, int length, SQLDataSetting... sqlDataSettings){
        this.name = name;
        this.sqlDataType = sqlDataType;
        this.length = length;
        this.sqlDataSettings = Lists.newArrayList(sqlDataSettings);
    }

    public boolean hasSetting(SQLDataSetting sqlDataSetting){
        return sqlDataSettings.contains(sqlDataSetting);
    }
}
