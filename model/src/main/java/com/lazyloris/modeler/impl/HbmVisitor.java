package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Interface;
import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

public class HbmVisitor extends FreemarkerAdaptor implements ModelVisitor {

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
                if (intf.getParent() != null)
                    continue;
                setFileName(intf.getName() + "Impl.hbm.xml");
                addParameter("interface", intf);
                render();
            }
        }
    }


}
