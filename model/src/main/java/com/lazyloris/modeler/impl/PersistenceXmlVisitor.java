package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

public class PersistenceXmlVisitor extends FreemarkerAdaptor implements ModelVisitor {

    @Override
    public void visit(Model model) {
        addParameter("model", model);
        for (Module m : model.getModules()) {
            setPackageName(m.getName() + ".entity");
            setOutputPath(getPackagePath());
            setFileName("persistence.xml");
            addParameter("module", m);
            render();
        }
    }

}
