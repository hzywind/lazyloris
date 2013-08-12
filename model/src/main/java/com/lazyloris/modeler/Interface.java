package com.lazyloris.modeler;

import java.util.List;

public interface Interface {

    String getName();

    String getFullName();

    Interface getParent();

    List<Attribute> getAttributes();

    List<Association> getAssociations();

    String getPackage();
}
