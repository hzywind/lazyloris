package com.lazyloris.modeler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.lazyloris.modeler.Model;
import com.lazyloris.modeler.ModelVisitor;

public class ModelVisitorImpl implements ModelVisitor {
	
	private List<ModelVisitor> visitors;
	
	@Required
	public void setVisitors(List<ModelVisitor> visitors) {
		this.visitors = visitors;
	}

	@Override
	public void visit(Model model) {
		for (ModelVisitor visitor : visitors) {
			visitor.visit(model);
		}
	}

}
