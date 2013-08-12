package com.lazyloris.model.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import com.lazyloris.model.Meta;

public class MetaImpl extends AbstractMeta {

    private static final long serialVersionUID = 1042030048148596297L;
    
    protected Properties data;
    boolean updated;
    private byte[] raw;

    public MetaImpl(byte[] bytes) {
        data = new Properties();
        raw = bytes;
        if (bytes != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            try {
                data.load(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public MetaImpl() {
        this(null);
    }

    @Override
    public String getString(String key) {
        return data.getProperty(key);
    }

    @Override
    public void put(String key, String value) {
        data.put(key, value);
        updated = true;
    }

    @Override
    public void remove(String key) {
        if (data.remove(key) != null)
            updated = true;
    }

    @Override
    public Meta copy() {
        return new MetaImpl(getBytes());
    }

    public byte[] getBytes() {
        if (updated) {
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            try {
                data.store(os, "Lazyloris Meta Implemented by "
                        + this.getClass().getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            raw = os.toByteArray();
            updated = false;
        }
        return raw;
    }

    @Override
    public int hashCode() {
        byte[] content = getBytes();
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        byte[] content = getBytes();
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetaImpl other = (MetaImpl) obj;
        if (content == null) {
            if (other.getBytes() != null)
                return false;
        } else if (!Arrays.equals(content, other.getBytes()))
            return false;
        return true;
    }

	@SuppressWarnings("rawtypes")
	@Override
	public Map getMap() {
		return data;
	}

}
