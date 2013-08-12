package com.lazyloris.modeler;

public interface Attribute {

    Type getType();

    String getName();

    boolean isReadOnly();

    String getDefault();

    boolean isUnique();
}
