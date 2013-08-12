package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Interface;
import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

public class EntityVisitor extends FreemarkerAdaptor implements ModelVisitor {

    @Override
    public void visit(Model model) {
        addParameter("model", model);
        addParameter("utils", new Utils());
        for (Module m : model.getModules()) {
            setPackageName(m.getName() + ".entity");
            setOutputPath(getPackagePath());
            
            addParameter("module", m);
            for (Interface intf : m.getInterfaces()) {
                setFileName(intf.getName() + "Entity.java");
                addParameter("interface", intf);
                render();
            }
        }
    }
}
