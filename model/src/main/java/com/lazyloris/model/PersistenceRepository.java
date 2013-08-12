/**
 * 
 */
package com.lazyloris.model;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author wind hong
 * 
 */
public interface PersistenceRepository {

	<T> T persist(T entity);

	<T> void remove(T entity);

	CriteriaBuilder getCriteriaBuilder();

	<T> List<T> executeQuery(CriteriaQuery<?> query);

	<T> List<T> executeQuery(CriteriaQuery<?> query, int first, int size);

    <T> List<T> executeQuery(String query, Object ... parameters);
    
    <T> List<T> executeQuery(String query, int first, int size, Object ... parameters);
    /*
     * if has match return first record else return null
     */
    <T> T executeFirstOrDefault(CriteriaQuery<?> query);
    /*
     * if has match return first record else return null
     */
    <T> T executeFirstOrDefault(String query, Object ... parameters);
}
