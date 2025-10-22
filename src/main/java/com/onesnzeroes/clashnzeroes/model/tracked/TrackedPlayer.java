package com.onesnzeroes.clashnzeroes.model.tracked;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name ="trackedplayer")
public class TrackedPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tag;
    private long ts;

    public TrackedPlayer(String tag) {
        this.tag = tag;
        this.ts = Instant.now().getEpochSecond();
    }

    public TrackedPlayer(){}

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

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "TrackedPlayer{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", ts=" + ts +
                '}';
    }
}
