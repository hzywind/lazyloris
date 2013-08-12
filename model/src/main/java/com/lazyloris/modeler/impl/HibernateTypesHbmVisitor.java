/**
 * 
 */
package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

/**
 * @author wind hong
 * 
 */
public class HibernateTypesHbmVisitor extends FreemarkerAdaptor implements
        ModelVisitor {

    /*
     * (non-Javadoc)
     * 
     * @see com.lazyloris.modeler.ModelVisitor#visit(com.lazyloris.modeler.Model)
     */
    @Override
    public void visit(Model model) {
        for (Module module : model.getModules()) {
            if (module.getEnums().size() > 0) {
                setPackageName(module.getName() + ".hibernate");
                setOutputPath(getPackagePath());
                addParameter("module", module);
                setFileName("types.hbm.xml");
                render();
            }
        }
    }

}
