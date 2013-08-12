/**
 * 
 */
package com.lazyloris.modeler;

/**
 * @author wind hong
 * 
 */
public interface SchemaExport {
    
    public static enum Database {
        MYSQL, MSSQL, ORACLE
    }

    void setOutputDirectory(String outputDir);

    void setDatabase(Database db);

    void setConfigurationFile(String confFile);

    void setDrop(boolean drop);

    void setCreate(boolean create);

    void export();
    
}
