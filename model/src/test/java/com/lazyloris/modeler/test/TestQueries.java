package com.lazyloris.modeler.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lorizz.modeler.test.model.Product;
import com.lorizz.modeler.test.model.Shop;
import com.lorizz.modeler.test.model.SubProduct;
import com.lorizz.modeler.test.model.entity.ProductEntity;
import com.lorizz.modeler.test.model.entity.ProductEntity_;
import com.lorizz.modeler.test.model.entity.SubProductEntity;
import com.lorizz.modeler.test.model.entity.SubProductEntity_;
import com.lorizz.modeler.test.model.repository.Repository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "testContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@DirtiesContext
public class TestQueries {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private Repository repository;

    @BeforeTransaction
    public void prepare() {
        prepareFindAll();
        prepareJoin();
    }
    
    private void prepareFindAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Shop shop1 = repository.createShop();
        shop1.setName("Lorizz Shop 1");
        shop1.setAllDays(true);
        em.persist(shop1);
        //
        Shop shop2 = repository.createShop();
        shop2.setName("Lorizz Shop 2");
        shop2.setAllDays(false);
        em.persist(shop2);
        //
        em.getTransaction().commit();
        em.close();
    }
    
    private void prepareJoin() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Product p1 = repository.createProduct();
        p1.setName("p1");
        //
        Product p2 = repository.createProduct();
        p2.setName("p2");
        //
        SubProduct sp1 = repository.createSubProduct();
        sp1.setName("sp1");
        //
        p2.getSubProducts().add(sp1);
        em.persist(p1);
        em.persist(p2);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    @Transactional
    public void test() {
        testFindAll();
        testJoin();
        testFindAllSQ();
        testJoinSQ();
    }
    
    private void testFindAll() {
        List<Shop> shops = repository.findAllShop();
        assertEquals(2, shops.size());
        assertEquals("Lorizz Shop 1", shops.get(0).getName());
    }
    
    private void testJoin() {
        CriteriaBuilder cb = repository.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> products = cq.from(ProductEntity.class);
        ListJoin<ProductEntity, SubProductEntity> joinSubProducts = products.join(ProductEntity_.subProducts);
        cq.where(cb.equal(joinSubProducts.get(SubProductEntity_.name), "sp1")); 
        //
        List<Product> ps = repository.executeQuery(cq);
        assertEquals(1, ps.size());
        assertEquals("p2", ps.get(0).getName());
        assertEquals("sp1", ps.get(0).getSubProducts().get(0).getName());
    }
    
    private void testFindAllSQ() {
    	List<Shop> shops = repository.executeQuery("select s from Shop s");
    	assertEquals(2, shops.size());
        assertEquals("Lorizz Shop 1", shops.get(0).getName());
    }
    
    private void testJoinSQ() {
    	String query = "select p from Product p join p.subProducts s where s.name = ?1";
    	//
        List<Product> ps = repository.executeQuery(query, "sp1");
        assertEquals(1, ps.size());
        assertEquals("p2", ps.get(0).getName());
        assertEquals("sp1", ps.get(0).getSubProducts().get(0).getName());
    }

    @AfterTransaction
    public void verify() {
        
    }
}
