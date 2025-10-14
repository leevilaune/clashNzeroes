package com.onesnzeroes.clashnzeroes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "player")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tag")
    private String tag;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("townHallLevel")
    @Column(name = "town_hall_level")
    private int townHallLevel;

    @JsonProperty("townHallWeaponLevel")
    @Column(name = "town_hall_weapon_level")
    private int townHallWeaponLevel;

    @JsonProperty("expLevel")
    @Column(name = "exp_level")
    private int expLevel;

    @JsonProperty("trophies")
    @Column(name = "trophies")
    private int trophies;

    @JsonProperty("bestTrophies")
    @Column(name = "best_trophies")
    private int bestTrophies;

    @JsonProperty("warStars")
    @Column(name = "war_stars")
    private int warStars;

    @JsonProperty("role")
    @Column(name = "role")
    private String role;

    @JsonProperty("warPreference")
    @Column(name = "war_preference")
    private String warPreference;

    @JsonProperty("donations")
    @Column(name = "donations")
    private int donations;

    @JsonProperty("donationsReceived")
    @Column(name = "donations_received")
    private int donationsReceived;

    @JsonProperty("clanCapitalContributions")
    @Column(name = "clan_capital_contributions")
    private long clanCapitalContributions;

    @ManyToOne
    @JoinColumn(name = "clan")
    private ClanEntity clan;

    @ManyToOne
    @JoinColumn(name = "league")
    @JsonProperty("leagueTier")
    private LeagueEntity league;

    @Column(name = "ts")
    private long ts;

    public PlayerEntity() {}

    public PlayerEntity(String tag, String name, int townHallLevel, int townHallWeaponLevel, int expLevel,
                        int trophies, int bestTrophies, int warStars, String role, String warPreference,
                        int donations, int donationsReceived, long clanCapitalContributions,
                        ClanEntity clan, LeagueEntity league) {
        this.tag = tag;
        this.name = name;
        this.townHallLevel = townHallLevel;
        this.townHallWeaponLevel = townHallWeaponLevel;
        this.expLevel = expLevel;
        this.trophies = trophies;
        this.bestTrophies = bestTrophies;
        this.warStars = warStars;
        this.role = role;
        this.warPreference = warPreference;
        this.donations = donations;
        this.donationsReceived = donationsReceived;
        this.clanCapitalContributions = clanCapitalContributions;
        this.clan = clan;
        this.league = league;
        this.ts = Instant.now().getEpochSecond();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTownHallLevel() {
        return townHallLevel;
    }

    public void setTownHallLevel(int townHallLevel) {
        this.townHallLevel = townHallLevel;
    }

    public int getTownHallWeaponLevel() {
        return townHallWeaponLevel;
    }

    public void setTownHallWeaponLevel(int townHallWeaponLevel) {
        this.townHallWeaponLevel = townHallWeaponLevel;
    }

    public int getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(int expLevel) {
        this.expLevel = expLevel;
    }

    public int getTrophies() {
        return trophies;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public int getBestTrophies() {
        return bestTrophies;
    }

    public void setBestTrophies(int bestTrophies) {
        this.bestTrophies = bestTrophies;
    }

    public int getWarStars() {
        return warStars;
    }

    public void setWarStars(int warStars) {
        this.warStars = warStars;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWarPreference() {
        return warPreference;
    }

    public void setWarPreference(String warPreference) {
        this.warPreference = warPreference;
    }

    public int getDonations() {
        return donations;
    }

    public void setDonations(int donations) {
        this.donations = donations;
    }

    public int getDonationsReceived() {
        return donationsReceived;
    }

    public void setDonationsReceived(int donationsReceived) {
        this.donationsReceived = donationsReceived;
    }

    public long getClanCapitalContributions() {
        return clanCapitalContributions;
    }

    public void setClanCapitalContributions(long clanCapitalContributions) {
        this.clanCapitalContributions = clanCapitalContributions;
    }

    public ClanEntity getClan() {
        return clan;
    }

    public void setClan(ClanEntity clan) {
        this.clan = clan;
    }

    public LeagueEntity getLeague() {
        return league;
    }

    public void setLeague(LeagueEntity league) {
        this.league = league;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", townHallLevel=" + townHallLevel +
                ", townHallWeaponLevel=" + townHallWeaponLevel +
                ", expLevel=" + expLevel +
                ", trophies=" + trophies +
                ", bestTrophies=" + bestTrophies +
                ", warStars=" + warStars +
                ", role='" + role + '\'' +
                ", warPreference='" + warPreference + '\'' +
                ", donations=" + donations +
                ", donationsReceived=" + donationsReceived +
                ", clanCapitalContributions=" + clanCapitalContributions +
                ", clanSnapshot=" + clan +
                ", leagueSnapshot=" + league +
                ", ts=" + ts +
                '}';
    }
}