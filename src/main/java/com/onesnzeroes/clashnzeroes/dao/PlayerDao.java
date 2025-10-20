package com.onesnzeroes.clashnzeroes.dao;

import com.onesnzeroes.clashnzeroes.datasource.MariaDbJpaConnection;
import com.onesnzeroes.clashnzeroes.model.TsField;
import com.onesnzeroes.clashnzeroes.model.player.ClanEntity;
import com.onesnzeroes.clashnzeroes.model.player.LeagueEntity;
import com.onesnzeroes.clashnzeroes.model.player.PlayerEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

import java.time.Instant;
import java.util.List;

public class PlayerDao {

    public void persist(PlayerEntity player) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            em.getTransaction().begin();

            ClanEntity clan = player.getClan();
            ClanEntity latestClan = null;
            if (clan != null) {
                if (clan.getId() > 0) {
                    latestClan = em.find(ClanEntity.class, clan.getId());
                }
                if (latestClan == null) {
                    em.persist(clan);
                    latestClan = clan;
                }
            }

            LeagueEntity league = player.getLeague();
            LeagueEntity latestLeague = null;
            if (league != null) {
                if (league.getId() > 0) {
                    latestLeague = em.find(LeagueEntity.class, league.getId());
                }
                if (latestLeague == null) {
                    em.persist(league);
                    latestLeague = league;
                }
            }

            PlayerEntity snapshot = getPlayerEntity(player, latestClan, latestLeague);
            em.persist(snapshot);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(PlayerEntity player) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(player);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public PlayerEntity findByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<PlayerEntity> query = em.createQuery(
                    "SELECT p FROM PlayerEntity p WHERE p.tag = :tag",
                    PlayerEntity.class);
            query.setParameter("tag", tag);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public PlayerEntity findLatestByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM PlayerEntity p WHERE p.tag = :tag ORDER BY p.ts DESC",
                            PlayerEntity.class)
                    .setParameter("tag", tag)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Integer> findTrophies(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT p.trophies FROM PlayerEntity p WHERE p.tag = :tag ORDER BY p.ts ASC",
                    Integer.class);
            query.setParameter("tag", tag);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Integer> findDonations(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT p.donations FROM PlayerEntity p WHERE p.tag = :tag ORDER BY p.ts ASC",
                    Integer.class);
            query.setParameter("tag", tag);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> findUniquePlayerTags() {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<String> query = em.createQuery(
                    "SELECT DISTINCT p.tag FROM PlayerEntity p", String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    public List<TsField<Integer>> findTrophiesWithTs(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT p.ts, p.trophies FROM PlayerEntity p WHERE p.tag = :tag ORDER BY p.ts ASC",
                    Object[].class);
            query.setParameter("tag", tag);
            List<Object[]> results = query.getResultList();

            return results.stream()
                    .map(r -> new TsField<>((long) r[0], (Integer) r[1]))
                    .toList();
        } finally {
            em.close();
        }
    }
    public List<TsField<Integer>> findDonationsWithTs(String tag) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT p.ts, p.donations FROM PlayerEntity p WHERE p.tag = :tag ORDER BY p.ts ASC",
                    Object[].class);
            query.setParameter("tag", tag);
            List<Object[]> results = query.getResultList();

            return results.stream()
                    .map(r -> new TsField<>((long) r[0], (Integer) r[1]))
                    .toList();
        } finally {
            em.close();
        }
    }
    public List<PlayerEntity> findAll() {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            TypedQuery<PlayerEntity> query = em.createQuery(
                    "SELECT p FROM PlayerEntity p", PlayerEntity.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(PlayerEntity player) {
        EntityManager em = MariaDbJpaConnection.getEntityManager();
        try {
            em.getTransaction().begin();
            PlayerEntity managed = em.merge(player); // ensure managed
            em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private boolean hasClanChanged(ClanEntity oldClan, ClanEntity newClan) {
        if (oldClan == null || newClan == null) return true;
        return (oldClan.getName() != null && !oldClan.getName().equals(newClan.getName()))
                || oldClan.getClanLevel() != newClan.getClanLevel()
                || !oldClan.getBadgeUrls().equals(newClan.getBadgeUrls());
    }

    private boolean hasLeagueChanged(LeagueEntity oldLeague, LeagueEntity newLeague) {
        if (oldLeague == null || newLeague == null) return true;
        return (oldLeague.getName() != null && !oldLeague.getName().equals(newLeague.getName()))
                || !oldLeague.getIconUrls().equals(newLeague.getIconUrls());
    }

    private PlayerEntity getPlayerEntity(PlayerEntity player, ClanEntity latestClan, LeagueEntity latestLeague) {
        PlayerEntity snapshot = new PlayerEntity();
        snapshot.setTag(player.getTag());
        snapshot.setName(player.getName());
        snapshot.setTownHallLevel(player.getTownHallLevel());
        snapshot.setTownHallWeaponLevel(player.getTownHallWeaponLevel());
        snapshot.setExpLevel(player.getExpLevel());
        snapshot.setTrophies(player.getTrophies());
        snapshot.setBestTrophies(player.getBestTrophies());
        snapshot.setWarStars(player.getWarStars());
        snapshot.setRole(player.getRole());
        snapshot.setWarPreference(player.getWarPreference());
        snapshot.setDonations(player.getDonations());
        snapshot.setDonationsReceived(player.getDonationsReceived());
        snapshot.setClanCapitalContributions(player.getClanCapitalContributions());
        snapshot.setClan(latestClan);
        snapshot.setLeague(latestLeague);
        snapshot.setTs(Instant.now().getEpochSecond());
        return snapshot;
    }
}