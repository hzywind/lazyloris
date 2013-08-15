package com.lazyloris.mcp.application.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class DerbyInitializer {

    private Log log = LogFactory.getLog(getClass());

    private String dbLocation;
    private String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    @Required
    public void setDbLocation(String location) {
        this.dbLocation = location;
    }

    public void init() {
        File dbFile = new File(dbLocation);
        if (dbFile.exists()) {
            if (!dbFile.isDirectory())
                dbFile.delete();
            else {
            	log.info("Database found, initialization skipped.");
                return;
            }
        }
        log.info("Creating database ... ");
        try {
            Class.forName(driver).newInstance();
            DriverManager.getConnection(
                    "jdbc:derby:" + dbLocation + ";create=true").close();
            
            createTables(DriverManager.getConnection(
                    "jdbc:derby:" + dbLocation));
            try {
                DriverManager.getConnection(
                        "jdbc:derby:" + dbLocation + ";shutdown=true").close();
            } catch (SQLException se) {
                if ("08006".equals(se.getSQLState())) {
                    log.debug("database shutdown.");
                } else {
                    throw se;
                }
            }
            log.info("Database created.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	private void createTables(Connection connection) {
		log.info("Creating tables ...");
	}
}
