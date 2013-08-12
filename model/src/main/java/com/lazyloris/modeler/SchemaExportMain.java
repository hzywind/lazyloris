/**
 * 
 */
package com.lazyloris.modeler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wind hong
 * 
 */
public class SchemaExportMain {

    public static void main(String[] args) {
        String confFile = args[0];
        String outputDir = args[1];

        ApplicationContext context = new ClassPathXmlApplicationContext(
                "com/lazyloris/modeler/schemaExportContext.xml");
        SchemaExporter exp = (SchemaExporter) context.getBean("exporter");
        exp.setConfFile(confFile);
        exp.setOutputDir(outputDir);
        exp.export();
    }

}
