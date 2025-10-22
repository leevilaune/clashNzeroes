package com.onesnzeroes.clashnzeroes.model.tracked;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "trackedwar")
public class TrackedWar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tag;

    @Column(name = "end_ts")
    private long endTs;
    private long ts;

    public TrackedWar(String tag, long endTs){
        this.tag = tag;
        this.endTs = endTs;
        this.ts = Instant.now().getEpochSecond();
    }

    public TrackedWar(){}

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

    public long getEndTs() {
        return endTs;
    }

    public void setEndTs(long endTs) {
        this.endTs = endTs;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackedWar that)) return false;
        return endTs == that.endTs
                && tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, endTs);
    }

    @Override
    public String toString() {
        return "TrackedWar{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", endTs=" + endTs +
                ", ts=" + ts +
                '}';
    }
}

