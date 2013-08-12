package com.lazyloris.modeler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lorizz.modeler.test.model.Book;
import com.lorizz.modeler.test.model.Product;
import com.lorizz.modeler.test.model.Software;
import com.lorizz.modeler.test.model.WebBrowser;
import com.lorizz.modeler.test.model.entity.BookEntity;
import com.lorizz.modeler.test.model.entity.ProductEntity;
import com.lorizz.modeler.test.model.entity.SoftwareEntity;
import com.lorizz.modeler.test.model.entity.WebBrowserEntity;
import com.lorizz.modeler.test.model.repository.Repository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "testContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@DirtiesContext
public class TestInheritance {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private Repository repository;

    @BeforeTransaction
    public void prepare() {
        Product p1 = new ProductEntity();
        p1.setName("p1");
        p1.setPrice(1.0);
        p1.setQuantity(1);
        //
        Software p2 = new SoftwareEntity();
        p2.setName("p2");
        p2.setPrice(2.0);
        p2.setQuantity(2);
        p2.setOs("Windows");
        p2.setVendor("MS");
        //
        Book p3 = new BookEntity();
        p3.setName("p3");
        p3.setPrice(3.0);
        p3.setQuantity(3);
        p3.setAuthor("Gavin King");
        p3.setDescription("Hibernate In Action");
        //
        WebBrowser p4 = new WebBrowserEntity();
        p4.setName("p4");
        p4.setPrice(4.0);
        p4.setQuantity(4);
        p4.setOs("Linux");
        p4.setVendor("Apache");
        p4.setHtml5Supported(true);
        //
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);
        em.persist(p4);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    @Transactional
    public void test() {
        List<Product> products = repository.findAllProduct();
        assertEquals(4, products.size());
        for (Product p : products) {
            if ("p1".equals(p.getName())) {
                assertEquals(1, p.getQuantity());
            } else if ("p2".equals(p.getName())) {
                assertEquals(2, p.getQuantity());
                assertTrue(p instanceof Software);
                assertEquals("MS", ((Software) p).getVendor());
            } else if ("p3".equals(p.getName())) {
                assertEquals(3, p.getQuantity());
                assertTrue(p instanceof Book);
                assertEquals("Gavin King", ((Book) p).getAuthor());
            } else {
                assertEquals(4, p.getQuantity());
                assertTrue(p instanceof WebBrowser);
                assertTrue(((WebBrowser) p).isHtml5Supported());
            }
        }
    }
}
