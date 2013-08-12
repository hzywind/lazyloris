package com.lazyloris.model;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class AbstractMutableType implements UserType {

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return (x == y) || (x != null && y != null && x.equals(y));
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return deepCopy(owner);
    }
}
