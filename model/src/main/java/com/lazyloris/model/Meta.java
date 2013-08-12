package com.lazyloris.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface Meta extends Serializable {

    boolean getBoolean(String key);

    String getString(String key);

    int getInt(String key);

    long getLong(String key);

    String[] getStrings(String key);

    Date getDate(String key);

    void put(String key, String value);

    void put(String key, boolean value);

    void put(String key, int value);

    void put(String key, long value);

    void put(String key, String[] value);

    void put(String key, Date value);

    void remove(String key);
    
    @SuppressWarnings("rawtypes")
    void addAll(Map map);
    
    @SuppressWarnings("rawtypes")
	Map getMap();

    Meta copy();
}
