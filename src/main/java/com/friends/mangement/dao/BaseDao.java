package com.friends.mangement.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * <PRE>
 * Class name       : BaseDao
 * Description      : This class is used manage object as entity to interact with database and service layer.
 * 
 * </PRE>
 */

public class BaseDao<E> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<E> entityClass;

    public BaseDao() {

    }

    public BaseDao(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public void create(E object) {
        entityManager.persist(object);

    }

    public void delete(E object) {
        if (entityManager.contains(object)) {
            entityManager.remove(object);
        } else {
            entityManager.remove(entityManager.merge(object));
        }

    }

    @SuppressWarnings("unchecked")
    public List<E> getAll() {
        return entityManager.createQuery("from " + entityClass.getName())
                .getResultList();
    }

    public E get(long id) {
        return entityManager.find(entityClass, id);
    }

    public void update(E object) {
        entityManager.merge(object);
        return;
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}
