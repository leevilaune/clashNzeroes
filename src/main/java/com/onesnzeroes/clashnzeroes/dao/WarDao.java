package com.onesnzeroes.clashnzeroes.dao;


import com.onesnzeroes.clashnzeroes.datasource.MariaDbJpaConnection;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class WarDao {

    public void persist(WarEntity war) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(war);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void update(WarEntity war) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(war);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void saveOrUpdate(WarEntity war) {
        if (findAll().contains(war)) update(war);
        else persist(war);
    }

    public WarEntity findById(Long id) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            return em.find(WarEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<WarEntity> findAll() {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<WarEntity> query = em.createQuery("SELECT w FROM WarEntity w", WarEntity.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<WarEntity> findByState(String state) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<WarEntity> query = em.createQuery(
                    "SELECT w FROM WarEntity w WHERE w.state = :state",
                    WarEntity.class
            );
            query.setParameter("state", state);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public WarEntity findLatest() {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<WarEntity> query = em.createQuery(
                    "SELECT w FROM WarEntity w ORDER BY w.endTime DESC",
                    WarEntity.class
            );
            query.setMaxResults(1);
            List<WarEntity> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            WarEntity war = em.find(WarEntity.class, id);
            if (war != null) {
                em.remove(war);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}