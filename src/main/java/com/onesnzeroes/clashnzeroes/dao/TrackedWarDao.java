package com.onesnzeroes.clashnzeroes.dao;

import com.onesnzeroes.clashnzeroes.datasource.MariaDbJpaConnection;
import com.onesnzeroes.clashnzeroes.model.player.PlayerEntity;
import com.onesnzeroes.clashnzeroes.model.tracked.TrackedWar;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TrackedWarDao {

    public void persist(TrackedWar war) {
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
    public void persistIfNotExists(TrackedWar war) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(w) FROM TrackedWar w WHERE w.tag = :tag AND w.endTs = :endTs", Long.class);
            query.setParameter("tag", war.getTag());
            query.setParameter("endTs", war.getEndTs());

            Long count = query.getSingleResult();
            if (count == 0) {
                em.persist(war);
            } else {
                System.out.println("TrackedWar already exists for clanTag " + war.getTag() +
                        " with endTs " + war.getEndTs());
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public TrackedWar findByTag(String tag, long endTs) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<TrackedWar> query = em.createQuery(
                    "SELECT p FROM TrackedWar p WHERE p.tag = :tag AND p.endTs = :endTs",
                    TrackedWar.class);
            query.setParameter("tag", tag);
            query.setParameter("endTs", endTs);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }
    public void deleteByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            em.getTransaction().begin();

            int deletedCount = em.createQuery(
                            "DELETE FROM TrackerWar p WHERE p.tag = :tag")
                    .setParameter("tag", tag)
                    .executeUpdate();

            System.out.println("Deleted " + deletedCount + " tracked wars(s) with tag " + tag);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public List<TrackedWar> findAll() {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<TrackedWar> query = em.createQuery("SELECT w FROM TrackedWar w", TrackedWar.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
