/**
 * 
 */
package com.lazyloris.modeler.impl;

import java.util.Map;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

/**
 * @author wind hong
 * 
 */
public class HibernateCfgVisitor extends FreemarkerAdaptor implements
        ModelVisitor {

    private Map<String, String> hibernateProperties;

    public void setHibernateProperties(Map<String, String> hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.lazyloris.modeler.ModelVisitor#visit(com.lazyloris.modeler.Model)
     */
    @Override
    public void visit(Model model) {
        addParameter("model", model);
        addParameter("utils", new Utils());
        addParameter("props", hibernateProperties);
        for (Module m : model.getModules()) {
            setPackageName(m.getName() + ".hibernate");
            setOutputPath(getPackagePath());
            addParameter("module", m);
            setFileName("hibernate.cfg.xml");
            render();
        }
    }

}
