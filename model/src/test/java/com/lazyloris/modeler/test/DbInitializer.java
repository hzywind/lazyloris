package com.lazyloris.modeler.test;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class DbInitializer {

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
            else
                return;
        }
        log.debug("creating database ... ");
        try {
            Class.forName(driver).newInstance();
            DriverManager.getConnection(
                    "jdbc:derby:" + dbLocation + ";create=true").close();
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
