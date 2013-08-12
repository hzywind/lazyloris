package com.lazyloris.modeler;

public interface Type {

    public static enum DataType {
        PRIMITIVE, ENUM, META, JAVA
    }

    DataType getDataType();

    Interface getInterface();

    String getName();
}
