package com.lazyloris.modeler.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.lazyloris.modeler.GenerateType;
import com.lazyloris.modeler.Generator;
import com.lazyloris.modeler.ModelAcceptor;
import com.lazyloris.modeler.ModelVisitor;

public class GeneratorImpl implements Generator {

    private Map<GenerateType, ModelVisitor> visitors;
    private ModelAcceptor acceptor;

    @Required
    public void setVisitors(Map<GenerateType, ModelVisitor> visitors) {
        this.visitors = visitors;
    }

    @Required
    public void setAcceptor(ModelAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    @Override
    public void generate(GenerateType type) {
        acceptor.accept(visitors.get(type));
    }

}
