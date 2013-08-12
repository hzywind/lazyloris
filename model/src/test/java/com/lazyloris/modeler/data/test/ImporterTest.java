/**
 * 
 */
package com.lazyloris.modeler.data.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lazyloris.modeler.data.Importer;
import com.lorizz.modeler.test.model.repository.Repository;

/**
 * @author wind hong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "testContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@DirtiesContext
public class ImporterTest {
    
    @Autowired
    private Importer<Repository> importer;
    
    @Transactional
    @Test
    public void test() {
        importer.doImport(this.getClass().getResourceAsStream("data.xml"));
    }
    
}
