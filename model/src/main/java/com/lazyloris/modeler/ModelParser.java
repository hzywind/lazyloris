package com.lazyloris.modeler;

import java.io.InputStream;
import java.util.List;

public interface ModelParser {

    List<Model> parse(InputStream is);

}
