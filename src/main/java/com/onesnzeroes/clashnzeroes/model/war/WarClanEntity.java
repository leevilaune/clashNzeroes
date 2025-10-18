package com.onesnzeroes.clashnzeroes.model.war;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onesnzeroes.clashnzeroes.model.player.BadgeUrls;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "warclan")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarClanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tag;
    private String name;

    @Column(name = "clan_level")
    private int clanLevel;

    private int attacks;
    private int stars;

    @Column(name = "destruction_percentage")
    private double destructionPercentage;

    @Embedded
    @JsonProperty("badgeUrls")
    private BadgeUrls badgeUrls;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "clan_id")
    private List<MemberEntity> members;

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

    public int getClanLevel() {
        return clanLevel;
    }

    public void setClanLevel(int clanLevel) {
        this.clanLevel = clanLevel;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public double getDestructionPercentage() {
        return destructionPercentage;
    }

    public void setDestructionPercentage(double destructionPercentage) {
        this.destructionPercentage = destructionPercentage;
    }

    public BadgeUrls getBadgeUrls() {
        return badgeUrls;
    }

    public void setBadgeUrls(BadgeUrls badgeUrls) {
        this.badgeUrls = badgeUrls;
    }

    public List<MemberEntity> getMembers() {
        return members;
    }

    public void setMembers(List<MemberEntity> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "ClanEntity{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", clanLevel=" + clanLevel +
                ", attacks=" + attacks +
                ", stars=" + stars +
                ", destructionPercentage=" + destructionPercentage +
                ", badgeUrls=" + badgeUrls +
                ", members=" + members +
                '}';
    }
}