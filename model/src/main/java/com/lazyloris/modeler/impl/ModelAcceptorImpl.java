package com.lazyloris.modeler.impl;

import java.io.FileInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.lazyloris.modeler.Env;
import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelAcceptor;
import com.lazyloris.modeler.ModelParser;
import com.lazyloris.modeler.ModelVisitor;

public class ModelAcceptorImpl implements ModelAcceptor {

	private ModelParser parser;
	private List<Model> models;

	@Required
	public void setParser(ModelParser parser) {
		this.parser = parser;
	}

	@Override
	public void accept(ModelVisitor visitor) {
		try {
			if (models == null)
				models = parser.parse(new FileInputStream(Env.getInstance().getModelFile()));
			for (Model model : models)
				visitor.visit(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
