package com.lazyloris.model.impl;

import java.io.Serializable;

import com.lazyloris.model.Entity;

public abstract class AbstractEntity<T, ID extends Serializable> implements
        Entity<T, ID> {

    protected ID id;

    @Override
    public ID getId() {
        return this.id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractEntity))
            return false;
        try {
            AbstractEntity<?, ?> e = (AbstractEntity<?, ?>) obj;
            if (e.getId() != null && e.getId().equals(getId()))
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
