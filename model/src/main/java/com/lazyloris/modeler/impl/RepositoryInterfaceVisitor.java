package com.lazyloris.modeler.impl;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;
import com.lazyloris.modeler.Module;

public class RepositoryInterfaceVisitor extends FreemarkerAdaptor implements ModelVisitor {

	@Override
	public void visit(Model model) {
        addParameter("model", model);
        for (Module m : model.getModules()) {
            setPackageName(m.getName() + ".repository");
            setOutputPath(getPackagePath());
            setFileName("Repository.java");
            addParameter("module", m);
            render();
        }
	}

}
