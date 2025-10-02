package edu.univ.scientometrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchMetadata(
        String id,
        String status,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("total_time_taken") Double totalTimeTaken
) {}