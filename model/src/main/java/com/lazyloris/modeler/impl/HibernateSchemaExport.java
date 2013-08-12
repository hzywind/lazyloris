/**
 * 
 */
package com.lazyloris.modeler.impl;

import java.io.File;
import java.util.Properties;

import org.hibernate.cfg.Configuration;

import com.lazyloris.modeler.SchemaExport;

/**
 * @author wind hong
 * 
 */
public class HibernateSchemaExport implements SchemaExport {

    private static final String DIALET_KEY = "hibernate.dialect";

    private String outputFile;
    private String outputDir;
    private String confFile;
    private boolean drop;
    private boolean create;
    private String dbDialet;
    private Properties properties;

    /*
     * (non-Javadoc)
     * 
     * @see com.lazyloris.modeler.SchemaExport#setOutputDirectory(java.lang.String)
     */
    @Override
    public void setOutputDirectory(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setOutputFile(String file) {
        outputFile = file;
    }

    public void setProperties(Properties prop) {
        this.properties = prop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lazyloris.modeler.SchemaExport#setConfigurationFile(java.lang.String)
     */
    @Override
    public void setConfigurationFile(String confFile) {
        this.confFile = confFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.lazyloris.modeler.SchemaExport#setDrop(boolean)
     */
    @Override
    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.lazyloris.modeler.SchemaExport#setCreate(boolean)
     */
    @Override
    public void setCreate(boolean create) {
        this.create = create;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.lazyloris.modeler.SchemaExport#setDatabase(com.lazyloris.modeler.SchemaExport
     * .Database)
     */
    @Override
    public void setDatabase(Database db) {
        switch (db) {
        case MYSQL:
            dbDialet = "org.hibernate.dialect.MySQL5Dialect";
            break;
        case MSSQL:
            dbDialet = "org.hibernate.dialect.SQLServer2008Dialect";
            break;
        case ORACLE:
            dbDialet = "org.hibernate.dialect.OracleDialect";
            break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.lazyloris.modeler.SchemaExport#export()
     */
    @Override
    public void export() {
        Configuration cfg = new Configuration();
        cfg.configure(new File(confFile));
        Properties props = new Properties();
        props.putAll(cfg.getProperties());
        props.putAll(properties);
        props.put(DIALET_KEY, dbDialet);
        cfg.setProperties(props);
        //
        File output = new File(outputDir, outputFile);
        if (!output.getParentFile().exists()) {
            output.getParentFile().mkdirs();
        }
        //
        new org.hibernate.tool.hbm2ddl.SchemaExport(cfg).setHaltOnError(true)
                .setOutputFile(output.getPath()).setDelimiter(";")
                .execute(true, false, drop, create);
    }

}
