package com.lazyloris.modeler;

import java.util.List;

public interface Module {

    String getName();

    List<Interface> getInterfaces();

    List<Enum> getEnums();
}
