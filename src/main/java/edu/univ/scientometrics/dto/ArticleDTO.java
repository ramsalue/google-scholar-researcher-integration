package edu.univ.scientometrics.dto;

/**
 * Data Transfer Object for Article information.
 *
 * This Record provides an immutable representation of article data
 * for transfer between application layers. Unlike ArticleEntity,
 * this is immutable and doesn't contain JPA annotations.
 *
 * SOLID Principles:
 * - Single Responsibility: Only transfers article data between layers
 * - Open/Closed: Records are final and cannot be extended
 *
 * Why use DTO instead of Entity directly?
 * 1. Decouples presentation layer from persistence layer
 * 2. Immutability prevents accidental modifications
 * 3. Can include computed fields not in database
 * 4. Prevents lazy loading issues in JSON serialization
 *
 * @param id Unique identifier
 * @param researcherId Foreign key to researcher
 * @param researcherName Name of the researcher (computed field)
 * @param title Article title
 * @param authors Comma-separated author names
 * @param publicationDate Publication date as string
 * @param abstractText Article abstract/summary
 * @param link URL to full article
 * @param keywords Comma-separated keywords
 * @param citedBy Number of citations
 * @param snippet Short description from Google Scholar
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
public record ArticleDTO(
        Long id,
        Long researcherId,
        String researcherName,
        String title,
        String authors,
        String publicationDate,
        String abstractText,
        String link,
        String keywords,
        Integer citedBy,
        String snippet
) {
    /**
     * Compact constructor with validation
     * Ensures required fields are not null
     */
    public ArticleDTO {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (researcherId == null) {
            throw new IllegalArgumentException("Researcher ID cannot be null");
        }
    }
}