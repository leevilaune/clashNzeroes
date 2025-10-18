package com.onesnzeroes.clashnzeroes.model.war;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "warattack")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attacker_tag")
    private String attackerTag;

    @Column(name = "defender_tag")
    private String defenderTag;

    private int stars;

    @Column(name = "destruction_percentage")
    private double destructionPercentage;

    @Column(name = "attack_order", nullable = false)
    @JsonProperty("order")
    private int order;    private int duration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttackerTag() {
        return attackerTag;
    }

    public void setAttackerTag(String attackerTag) {
        this.attackerTag = attackerTag;
    }

    public String getDefenderTag() {
        return defenderTag;
    }

    public void setDefenderTag(String defenderTag) {
        this.defenderTag = defenderTag;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AttackEntity{" +
                "id=" + id +
                ", attackerTag='" + attackerTag + '\'' +
                ", defenderTag='" + defenderTag + '\'' +
                ", stars=" + stars +
                ", destructionPercentage=" + destructionPercentage +
                ", order=" + order +
                ", duration=" + duration +
                '}';
    }
}