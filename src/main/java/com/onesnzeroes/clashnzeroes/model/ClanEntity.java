package com.onesnzeroes.clashnzeroes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "clan")
public class ClanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tag")
    private String tag;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("clanLevel")
    @Column(name = "clan_level")
    private int clanLevel;

    @Embedded
    @JsonProperty("badgeUrls")
    private BadgeUrls badgeUrls;

    public ClanEntity() {}

    public ClanEntity(String tag, String name, int clanLevel, BadgeUrls badgeUrls) {
        this.tag = tag;
        this.name = name;
        this.clanLevel = clanLevel;
        this.badgeUrls = badgeUrls;
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

    public int getClanLevel() {
        return clanLevel;
    }

    public void setClanLevel(int clanLevel) {
        this.clanLevel = clanLevel;
    }

    public BadgeUrls getBadgeUrls() {
        return badgeUrls;
    }

    public void setBadgeUrls(BadgeUrls badgeUrls) {
        this.badgeUrls = badgeUrls;
    }

    @Override
    public String toString() {
        return "ClanEntity{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", clanLevel=" + clanLevel +
                ", badgeUrls=" + badgeUrls +
                '}';
    }
}