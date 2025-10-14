package com.onesnzeroes.clashnzeroes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "league")
public class LeagueEntity {

    @Id
    @Column(name = "id")
    private long id;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @Embedded
    @JsonProperty("iconUrls")
    private IconUrls iconUrls;

    public LeagueEntity(long id, String name, IconUrls iconUrls) {
        this.id = id;
        this.name = name;
        this.iconUrls = iconUrls;
    }

    public LeagueEntity(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IconUrls getIconUrls() {
        return iconUrls;
    }

    public void setIconUrls(IconUrls iconUrls) {
        this.iconUrls = iconUrls;
    }

    @Override
    public String toString() {
        return "LeagueEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrls=" + iconUrls +
                '}';
    }
}