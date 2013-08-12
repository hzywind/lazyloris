/**
 * 
 */
package com.lazyloris.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.lazyloris.model.PersistenceRepository;

/**
 * @author wind hong
 * 
 */
public abstract class AbstractRepository implements PersistenceRepository {

	protected EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <T> T persist(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Override
	public <T> void remove(T entity) {
		entityManager.remove(entity);
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> executeQuery(CriteriaQuery<?> query) {
		List<T> result = new ArrayList<T>();
		for (Object o : entityManager.createQuery(query).getResultList()) {
			result.add((T) o);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> executeQuery(CriteriaQuery<?> query, int first, int size) {
		TypedQuery<?> q = entityManager.createQuery(query);
		if (first > -1)
			q.setFirstResult(first);
		if (size > 0)
			q.setMaxResults(size);
		List<T> result = new ArrayList<T>();
		for (Object o : q.getResultList()) {
			result.add((T) o);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> executeQuery(String queryString, Object... parameters) {
		Query query = createQuery(queryString, parameters);
		List<T> result = new ArrayList<T>();
		for (Object o : query.getResultList()) {
			result.add((T) o);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> executeQuery(String queryString, int first, int size,
			Object... parameters) {
		Query query = createQuery(queryString, parameters);
		if (first > -1)
			query.setFirstResult(first);
		if (size > 0)
			query.setMaxResults(size);
		List<T> result = new ArrayList<T>();
		for (Object o : query.getResultList()) {
			result.add((T) o);
		}
		return result;
	}

	/*
	 * if has match return first record else return null
	 */
	@SuppressWarnings("unchecked")
	public <T> T executeFirstOrDefault(CriteriaQuery<?> query) {
		List<?> result = entityManager.createQuery(query).getResultList();
		if(!result.isEmpty())
		{
			return (T)result.get(0);
		}
		return null;
	}

	/*
	 * if has match return first record else return null
	 */
	@SuppressWarnings("unchecked")
	public <T> T executeFirstOrDefault(String queryString, Object... parameters) {
		Query query = createQuery(queryString, parameters);
		List<?> result = query.getResultList();
		if(!result.isEmpty())
		{
			return (T)result.get(0);
		}
		return null;
	}

	private Query createQuery(String queryString, Object... parameters) {
		Query query = entityManager.createQuery(queryString);
		int pos = 1;
		for (Object param : parameters) {
			if (param instanceof Date) {
				query.setParameter(pos, (Date) param, TemporalType.TIMESTAMP);
			} else {
				query.setParameter(pos, param);
			}
			pos++;
		}
		return query;
	}

}
