/**
 * 
 */
package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Interface;
import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

/**
 * @author wind hong
 *
 */
public class HibernateImplVisitor extends FreemarkerAdaptor implements ModelVisitor {

    /* (non-Javadoc)
     * @see com.lazyloris.modeler.ModelVisitor#visit(com.lazyloris.modeler.Model)
     */
    @Override
    public void visit(Model model) {
        addParameter("model", model);
        addParameter("utils", new Utils());
        for (Module m : model.getModules()) {
            setPackageName(m.getName() + ".hibernate");
            setOutputPath(getPackagePath());
            
            addParameter("module", m);
            for (Interface intf : m.getInterfaces()) {
                setFileName(intf.getName() + "Impl.java");
                addParameter("interface", intf);
                render();
            }
        }
    }

}
