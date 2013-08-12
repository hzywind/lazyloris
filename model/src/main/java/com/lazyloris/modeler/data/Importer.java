/**
 * 
 */
package com.lazyloris.modeler.data;

import java.io.InputStream;

import com.lazyloris.model.PersistenceRepository;

/**
 * @author wind hong
 * 
 */
public interface Importer<T extends PersistenceRepository> {

    void doImport(InputStream is);

}
