package com.onesnzeroes.clashnzeroes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class IconUrls {

    @JsonProperty("small")
    private String small;

    @JsonProperty("large")
    private String large;

    public IconUrls(String small, String large) {
        this.small = small;
        this.large = large;
    }

    public IconUrls(){

    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    @Override
    public String toString() {
        return "IconUrls{" +
                "small='" + small + '\'' +
                ", large='" + large + '\'' +
                '}';
    }
}