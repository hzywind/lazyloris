package com.lazyloris.model.hibernate;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.lazyloris.model.AbstractMutableType;
import com.lazyloris.model.Meta;
import com.lazyloris.model.impl.MetaImpl;

public class BlobMetaType extends AbstractMutableType {

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null)
            return null;
        return ((Meta) value).copy();
    }


	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Blob blob = rs.getBlob(names[0]);
        return (blob == null) ? new MetaImpl() : new MetaImpl(blob.getBytes(1,
                (int) blob.length()));
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		MetaImpl meta = (MetaImpl) value;
        if (meta.getBytes() == null)
            st.setNull(index, Types.BLOB);
        else
            st.setBlob(index, new ByteArrayInputStream(meta.getBytes()));
	}

    @Override
    public Class<Meta> returnedClass() {
        return Meta.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.BLOB };
    }

}
