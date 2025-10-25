package com.onesnzeroes.clashnzeroes.dao;


import com.onesnzeroes.clashnzeroes.datasource.MariaDbJpaConnection;
import com.onesnzeroes.clashnzeroes.model.war.AttackEntity;
import com.onesnzeroes.clashnzeroes.model.war.MemberEntity;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Objects;

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

    public List<AttackEntity> findAttacksByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<AttackEntity> query = em.createQuery(
                    "SELECT a FROM AttackEntity a WHERE a.attackerTag = :tag",
                    AttackEntity.class
            );
            query.setParameter("tag", tag);
            return query.getResultList().stream().distinct().toList();
        } finally {
            em.close();
        }
    }

    public List<AttackEntity> findDefencesByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<AttackEntity> query = em.createQuery(
                    "SELECT a FROM AttackEntity a WHERE a.defenderTag = :tag",
                    AttackEntity.class
            );
            query.setParameter("tag", tag);
            return query.getResultList().stream().distinct().toList();
        } finally {
            em.close();
        }
    }
    public double[] getAttackPercentages(String tag) {
        return findAttacksByTag(tag)
                .stream()
                .mapToDouble(AttackEntity::getDestructionPercentage)
                .toArray();
    }

    public double[] getAttackDurations(String tag) {
        return findAttacksByTag(tag)
                .stream()
                .mapToDouble(AttackEntity::getDuration)
                .toArray();
    }

    public double[] getDefencePercentages(String tag) {
        return findDefencesByTag(tag)
                .stream()
                .mapToDouble(AttackEntity::getDestructionPercentage)
                .toArray();
    }

    public double[] getDefenceDurations(String tag) {
        return findDefencesByTag(tag)
                .stream()
                .mapToDouble(AttackEntity::getDuration)
                .toArray();
    }

    public Integer getTownHallLevel(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT p.townhallLevel FROM WarPlayerEntity p WHERE p.tag = :tag", Integer.class);
            query.setParameter("tag", tag);
            List<Integer> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public MemberEntity findWarPlayerByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<MemberEntity> query = em.createQuery(
                    "SELECT p FROM MemberEntity p WHERE p.tag = :tag", MemberEntity.class);
            query.setParameter("tag", tag);
            List<MemberEntity> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public double[] getPlayerTownHalls(String playerTag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            List<AttackEntity> attacks = em.createQuery(
                            "SELECT a FROM AttackEntity a WHERE a.attackerTag = :tag",
                            AttackEntity.class)
                    .setParameter("tag", playerTag)
                    .getResultList().stream().distinct().toList();

            return attacks.stream()
                    .mapToDouble(a -> {
                        List<Integer> thList = em.createQuery(
                                        "SELECT p.townhallLevel FROM MemberEntity p WHERE p.tag = :tag",
                                        Integer.class)
                                .setParameter("tag", a.getAttackerTag())
                                .getResultList();
                        return thList.isEmpty() ? 0 : thList.get(0);
                    })
                    .toArray();
        } finally {
            em.close();
        }
    }

    public double[] getAttackerTownHalls(String defenderTag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<AttackEntity> attackQuery = em.createQuery(
                    "SELECT a FROM AttackEntity a WHERE a.defenderTag = :tag",
                    AttackEntity.class
            );
            attackQuery.setParameter("tag", defenderTag);
            List<AttackEntity> attacks = attackQuery.getResultList().stream().distinct().toList();

            return attacks.stream()
                    .mapToDouble(a -> {
                        TypedQuery<Integer> thQuery = em.createQuery(
                                "SELECT p.townhallLevel FROM MemberEntity p WHERE p.tag = :tag",
                                Integer.class
                        );
                        thQuery.setParameter("tag", a.getAttackerTag());
                        List<Integer> thResult = thQuery.getResultList();
                        return thResult.isEmpty() ? null : thResult.get(0);
                    })
                    .filter(Objects::nonNull)
                    .toArray();
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