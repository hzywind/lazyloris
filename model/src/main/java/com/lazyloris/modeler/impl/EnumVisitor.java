package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

public class EnumVisitor extends FreemarkerAdaptor implements ModelVisitor {

    @Override
    public void visit(Model model) {
        addParameter("model", model);
        addParameter("utils", new Utils());
        for (Module m : model.getModules()) {
            setPackageName(m.getName());
            setOutputPath(getPackagePath());
            
            addParameter("module", m);
            for (com.lazyloris.modeler.Enum e : m.getEnums()) {
                setFileName(e.getName() + ".java");
                addParameter("enum", e);
                render();
            }
        }
    }

}
