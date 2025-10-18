package com.onesnzeroes.clashnzeroes.model.war;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "warplayer")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tag;
    private String name;

    @Column(name = "townhall_level")
    private int townhallLevel;

    @Column(name = "map_position")
    private int mapPosition;

    @Column(name = "opponent_attacks")
    private int opponentAttacks;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<AttackEntity> attacks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "best_opponent_attack_id")
    private AttackEntity bestOpponentAttack;

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

    public int getTownhallLevel() {
        return townhallLevel;
    }

    public void setTownhallLevel(int townhallLevel) {
        this.townhallLevel = townhallLevel;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
    }

    public int getOpponentAttacks() {
        return opponentAttacks;
    }

    public void setOpponentAttacks(int opponentAttacks) {
        this.opponentAttacks = opponentAttacks;
    }

    public List<AttackEntity> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<AttackEntity> attacks) {
        this.attacks = attacks;
    }

    public AttackEntity getBestOpponentAttack() {
        return bestOpponentAttack;
    }

    public void setBestOpponentAttack(AttackEntity bestOpponentAttack) {
        this.bestOpponentAttack = bestOpponentAttack;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", townhallLevel=" + townhallLevel +
                ", mapPosition=" + mapPosition +
                ", opponentAttacks=" + opponentAttacks +
                ", attacks=" + attacks +
                ", bestOpponentAttack=" + bestOpponentAttack +
                '}';
    }
}