package com.onesnzeroes.clashnzeroes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class BadgeUrls {

    @JsonProperty("small")
    private String small;

    @JsonProperty("medium")
    private String medium;

    @JsonProperty("large")
    private String large;

    // --- Getters and Setters ---
}