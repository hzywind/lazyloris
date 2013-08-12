package com.lazyloris.modeler;

import java.util.List;

public interface Association {

    public static enum Type {
        MANY, SINGLE, COMPOSITE
    }

    Interface getInterface();

    Type getType();

    Association getEnd();

    String getName();
    
    List<String> getStereotypes();
}
