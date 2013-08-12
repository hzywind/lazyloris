package com.lazyloris.modeler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lorizz.modeler.test.model.Category;
import com.lorizz.modeler.test.model.Shop;
import com.lorizz.modeler.test.model.State;
import com.lorizz.modeler.test.model.entity.CategoryEntity;
import com.lorizz.modeler.test.model.entity.ShopEntity;
import com.lorizz.modeler.test.model.repository.Repository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "testContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@DirtiesContext
public class TestModels {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private Repository repository;
    private Long shop1Id;
    private Long shop2Id;

    @Test
    @Transactional
    public void test() {
        testSave();
        testCascade();
    }

    private void testSave() {
        log.info("====================Save==================");
        Shop shop1 = repository.createShop();
        shop1.setAllDays(true);
        shop1.setName("Lorizz Shop 1");
        shop1.setState(State.CLOSED);
        shop1.getMeta().put("key1", false);
        shop1.getMeta().put("key2", "key2Value");
        repository.persist(shop1);
        shop1Id = shop1.getId();
    }

    private void testCascade() {
        log.info("====================Cascade==================");
        Shop shop2 = repository.createShop();
        shop2.setName("Lorizz Shop 2");
        Category cate1 = repository.createCategory();
        cate1.setName("Computer");
        Category cate2 = cate1.createCategory();
        cate2.setName("PC");
        Category cate3 = repository.createCategory();
        cate3.setName("Mobile");
        shop2.getCategories().add(cate1);
        shop2.getCategories().add(cate3);
        repository.persist(shop2);
        shop2Id = shop2.getId();
    }

    @AfterTransaction
    public void verify() {
        verifySave();
        verifyCascade();
    }

    private void verifySave() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Shop shop1 = em.find(ShopEntity.class, shop1Id);
        assertNotNull(shop1);
        assertEquals("Lorizz Shop 1", shop1.getName());
        assertEquals(State.CLOSED, shop1.getState());
        assertTrue(shop1.isAllDays());
        assertTrue(!shop1.getMeta().getBoolean("key1"));
        assertEquals("key2Value", shop1.getMeta().getString("key2"));
        em.getTransaction().rollback();
        em.close();
    }

    private void verifyCascade() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Shop shop2 = em.find(ShopEntity.class, shop2Id);
        List<Category> cates = shop2.getCategories();
        assertEquals("Computer", cates.get(0).getName());
        assertEquals("PC", cates.get(0).getChildren().get(0).getName());
        assertEquals("Mobile", cates.get(1).getName());
        Category cate1 = cates.get(0);
        cates.remove(cate1);
        em.remove(cate1);
        em.getTransaction().commit();
        em.close();

        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        shop2 = em.find(ShopEntity.class, shop2Id);
        cates = shop2.getCategories();
        assertEquals("Mobile", cates.get(0).getName());
        assertEquals(0, cates.get(0).getChildren().size());
        em.remove(shop2);
        em.getTransaction().commit();
        em.close();

        em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CategoryEntity> cq = cb.createQuery(CategoryEntity.class);
        cq.from(CategoryEntity.class);
        List<CategoryEntity> cates1 = em.createQuery(cq).getResultList();
        assertEquals(1, cates1.size());
        assertEquals("Mobile", cates1.get(0).getName());
        em.getTransaction().rollback();
        em.close();
    }

}
