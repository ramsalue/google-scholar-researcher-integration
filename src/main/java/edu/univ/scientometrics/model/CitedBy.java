package edu.univ.scientometrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CitedBy(
        Integer total,
        String link
) {}