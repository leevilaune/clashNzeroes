package com.onesnzeroes.clashnzeroes.model.war;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.onesnzeroes.clashnzeroes.util.ClashTimeDeserializer;
import jakarta.persistence.*;

@Entity
@Table(name = "war")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String state;

    @Column(name = "team_size")
    private int teamSize;

    @Column(name = "attacks_per_member")
    private int attacksPerMember;

    @Column(name = "battle_modifier")
    private String battleModifier;

    @Column(name = "preparation_start_time")
    @JsonDeserialize(using = ClashTimeDeserializer.class)
    private long preparationStartTime;

    @Column(name = "start_time")
    @JsonDeserialize(using = ClashTimeDeserializer.class)
    private long startTime;

    @Column(name = "end_time")
    @JsonDeserialize(using = ClashTimeDeserializer.class)
    private long endTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clan_id")
    private WarClanEntity clan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "opponent_id")
    private WarClanEntity opponent;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public int getAttacksPerMember() {
        return attacksPerMember;
    }

    public void setAttacksPerMember(int attacksPerMember) {
        this.attacksPerMember = attacksPerMember;
    }

    public String getBattleModifier() {
        return battleModifier;
    }

    public void setBattleModifier(String battleModifier) {
        this.battleModifier = battleModifier;
    }

    public long getPreparationStartTime() {
        return preparationStartTime;
    }

    public void setPreparationStartTime(long preparationStartTime) {
        this.preparationStartTime = preparationStartTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public WarClanEntity getClan() {
        return clan;
    }

    public void setClan(WarClanEntity clan) {
        this.clan = clan;
    }

    public WarClanEntity getOpponent() {
        return opponent;
    }

    public void setOpponent(WarClanEntity opponent) {
        this.opponent = opponent;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        WarEntity other = (WarEntity) obj;

        if (this.endTime != other.endTime) return false;

        String thisClanTag = this.clan != null ? this.clan.getTag() : null;
        String thisOpponentTag = this.opponent != null ? this.opponent.getTag() : null;
        String otherClanTag = other.clan != null ? other.clan.getTag() : null;
        String otherOpponentTag = other.opponent != null ? other.opponent.getTag() : null;

        return (thisClanTag != null && thisOpponentTag != null &&
        otherClanTag != null && otherOpponentTag != null) &&
        ((thisClanTag.equals(otherClanTag) && thisOpponentTag.equals(otherOpponentTag)) ||
         (thisClanTag.equals(otherOpponentTag) && thisOpponentTag.equals(otherClanTag)));
    }

    @Override
    public String toString() {
        return "WarEntity{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", teamSize=" + teamSize +
                ", attacksPerMember=" + attacksPerMember +
                ", battleModifier='" + battleModifier + '\'' +
                ", preparationStartTime=" + preparationStartTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", clan=" + clan +
                ", opponent=" + opponent +
                '}';
    }

}