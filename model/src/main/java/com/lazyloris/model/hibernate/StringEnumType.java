package com.lazyloris.model.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.internal.util.ReflectHelper;

import com.lazyloris.model.AbstractMutableType;

@SuppressWarnings({"unchecked", "rawtypes"})
public class StringEnumType extends AbstractMutableType implements
		EnhancedUserType, ParameterizedType {

	private Class<Enum> enumClass;

	@Override
	public Object fromXMLString(String xmlValue) {
		return Enum.valueOf(enumClass, xmlValue);
	}

	@Override
	public String objectToSQLString(Object value) {
		return '\'' + ((Enum) value).name() + '\'';
	}

	@Override
	public String toXMLString(Object value) {
		return ((Enum) value).name();
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String name = rs.getString(names[0]);
		return rs.wasNull() ? null : Enum.valueOf(enumClass, name);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, StandardBasicTypes.STRING.sqlType());
		} else {
			st.setString(index, ((Enum) value).name());
		}
	}

	@Override
	public Class returnedClass() {
		return enumClass;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { StandardBasicTypes.STRING.sqlType() };
	}

	@Override
	public void setParameterValues(Properties parameters) {
		String enumClassName = parameters.getProperty("enumClassname");
		try {
			enumClass = ReflectHelper.classForName(enumClassName);
		} catch (ClassNotFoundException cnfe) {
			throw new HibernateException("Enum class not found", cnfe);
		}
	}

}
