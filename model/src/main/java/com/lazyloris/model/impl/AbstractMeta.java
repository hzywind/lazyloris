package com.lazyloris.model.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.lazyloris.model.Meta;

public abstract class AbstractMeta implements Meta {

    private static final long serialVersionUID = 1814934942625236524L;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public boolean getBoolean(String key) {
        return Boolean.valueOf(getString(key));
    }

    @Override
    public Date getDate(String key) {
        try {
            return dateFormat.parse(getString(key));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getInt(String key) {
        return Integer.valueOf(getString(key));
    }

    @Override
    public long getLong(String key) {
        return Long.valueOf(getString(key));
    }

    @Override
    public String[] getStrings(String key) {
        return getString(key).split(",");
    }

    @Override
    public void put(String key, boolean value) {
        put(key, Boolean.toString(value));
    }

    @Override
    public void put(String key, int value) {
        put(key, Integer.toString(value));
    }

    @Override
    public void put(String key, long value) {
        put(key, Long.toString(value));
    }

    @Override
    public void put(String key, String[] value) {
        String value1 = "";
        for (String item : value) {
            if (value1.length() == 0)
                value1 += item;
            else
                value1 += "," + item;
        }
        put(key, value1);
    }

    @Override
    public void put(String key, Date value) {
        put(key, dateFormat.format(value));
    }

    @SuppressWarnings("rawtypes")
    @Override
	public void addAll(Map map) {
		for (Object key : map.keySet()) {
			Object obj = map.get(key);
			String skey = key.toString();
			if (obj instanceof Boolean) {
				put(skey, (Boolean) obj);
			} else if (obj instanceof Integer) {
				put(skey, (Integer) obj);
			} else if (obj instanceof Long) {
				put(skey, (Long) obj);
			} else if (obj instanceof String[]) {
				put(skey, (String[]) obj);
			} else if (obj instanceof Date) {
				put(skey, (Date) obj);
			} else {
				put(skey, obj.toString());
			}
		}
	}

}
