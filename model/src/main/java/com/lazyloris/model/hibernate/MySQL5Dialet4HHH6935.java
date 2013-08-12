package com.lazyloris.model.hibernate;

import java.sql.Types;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQL5Dialet4HHH6935 extends MySQL5Dialect {
	
	public MySQL5Dialet4HHH6935() {
		super();
		registerColumnType(Types.BOOLEAN, "bit(1)");
	}
	
}
