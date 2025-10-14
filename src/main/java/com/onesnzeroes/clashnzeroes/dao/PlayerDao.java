package com.onesnzeroes.clashnzeroes.dao;

import com.onesnzeroes.clashnzeroes.datasource.MariaDbJpaConnection;
import com.onesnzeroes.clashnzeroes.model.ClanEntity;
import com.onesnzeroes.clashnzeroes.model.LeagueEntity;
import com.onesnzeroes.clashnzeroes.model.PlayerEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.List;

public class PlayerDao {

    public void persist(PlayerEntity player) {
        EntityManager em = MariaDbJpaConnection.getInstance();

        ClanEntity clan = player.getClan();
        ClanEntity latestClan = null;
        if (clan != null) {
            if (clan.getId() > 0) {
                latestClan = em.find(ClanEntity.class, clan.getId());
            }
            if (latestClan == null) {
                em.getTransaction().begin();
                em.persist(clan);
                em.getTransaction().commit();
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
                em.getTransaction().begin();
                em.persist(league);
                em.getTransaction().commit();
                latestLeague = league;
            }
        }

        PlayerEntity snapshot = getPlayerEntity(player, latestClan, latestLeague);

        em.getTransaction().begin();
        em.persist(snapshot);
        em.getTransaction().commit();
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

    public void update(PlayerEntity player) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(player);
        em.getTransaction().commit();
    }

    public PlayerEntity findByTag(String tag) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        TypedQuery<PlayerEntity> query = em.createQuery(
                "SELECT p FROM PlayerEntity p WHERE p.tag = :tag", PlayerEntity.class);
        query.setParameter("tag", tag);
        return query.getResultStream().findFirst().orElse(null);
    }

    public List<PlayerEntity> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        TypedQuery<PlayerEntity> query = em.createQuery(
                "SELECT p FROM PlayerEntity p", PlayerEntity.class);
        return query.getResultList();
    }

    public void delete(PlayerEntity player) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        PlayerEntity managed = em.merge(player); // ensure managed
        em.remove(managed);
        em.getTransaction().commit();
    }
    private boolean hasClanChanged(ClanEntity oldClan, ClanEntity newClan) {
        if (oldClan == null || newClan == null) return true;
        return oldClan.getName() != null && !oldClan.getName().equals(newClan.getName())
                || oldClan.getClanLevel() != newClan.getClanLevel()
                || !oldClan.getBadgeUrls().equals(newClan.getBadgeUrls());
    }
    private boolean hasLeagueChanged(LeagueEntity oldLeague, LeagueEntity newLeague) {
        if (oldLeague == null || newLeague == null) return true;
        return oldLeague.getName() != null && !oldLeague.getName().equals(newLeague.getName())
                || !oldLeague.getIconUrls().equals(newLeague.getIconUrls());
    }
}
