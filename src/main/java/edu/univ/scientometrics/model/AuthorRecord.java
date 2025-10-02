// ============================================
// 3. MODEL LAYER (Records - Immutable Data)
// ============================================
package edu.univ.scientometrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorRecord(
        @JsonProperty("author_id") String authorId,
        String name,
        String affiliations,
        String email,
        @JsonProperty("cited_by") Integer citedBy,
        List<PublicationRecord> publications
) {}