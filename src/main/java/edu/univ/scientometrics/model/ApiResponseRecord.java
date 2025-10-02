// ============================================
// 3. MODEL LAYER (Records - Immutable Data)
// ============================================
package edu.univ.scientometrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponseRecord(
        @JsonProperty("search_metadata") SearchMetadata searchMetadata,
        @JsonProperty("organic_results") List<PublicationRecord> organicResults,
        String error
) {}
