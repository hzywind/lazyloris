/**
 * 
 */
package com.lazyloris.modeler;

import java.util.List;

/**
 * @author wind hong
 *
 */
public class SchemaExporter {
    
    private List<SchemaExport> exporters;
    private String outputDir;
    private String confFile;

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setConfFile(String confFile) {
        this.confFile = confFile;
    }

    public void setExporters(List<SchemaExport> exporters) {
        this.exporters = exporters;
    }
    
    public void export() {
        for (SchemaExport exp : exporters) {
            exp.setOutputDirectory(outputDir);
            exp.setConfigurationFile(confFile);
            exp.export();
        }
    }

}
