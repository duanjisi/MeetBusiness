package com.atgc.cotton.db;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/10.
 */
public class CityEntityColumn extends DatabaseColumn {

    public static final String TABLE_NAME = "cityTable";
    public static final String REGION_ID = "id";
    public static final String REGION_NAME = "name";

    private static final Map<String, String> mColumnMap = new HashMap<String, String>();

    static {
        mColumnMap.put(_ID, "integer primary key autoincrement");
        mColumnMap.put(REGION_ID, "text");
        mColumnMap.put(REGION_NAME, "text");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Map<String, String> getTableMap() {
        return mColumnMap;
    }
}
