// ============================================
// 3. MODEL LAYER (Records - Immutable Data)
// ============================================
package edu.univ.scientometrics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicationRecord(
        String title,
        String link,
        @JsonProperty("publication_info") PublicationInfo publicationInfo,
        String snippet,
        @JsonProperty("inline_links") InlineLinks inlineLinks
) {}