package edu.univ.scientometrics.util;

import edu.univ.scientometrics.dto.ArticleDTO;
import edu.univ.scientometrics.entity.ArticleEntity;
import edu.univ.scientometrics.entity.ResearcherEntity;
import edu.univ.scientometrics.model.PublicationRecord;
import edu.univ.scientometrics.model.AuthorInfo;

import java.util.stream.Collectors;

/**
 * Utility class for mapping between different data representations.
 *
 * Converts between:
 * - API Records (from Google Scholar)
 * - JPA Entities (for database)
 * - DTOs (for transfer between layers)
 *
 * SOLID Principles:
 * - Single Responsibility: Only performs data transformations
 * - Open/Closed: Can add new mapping methods without modifying existing ones
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
public class DataMapper {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private DataMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts PublicationRecord (from API) to ArticleEntity (for database).
     *
     * Extracts relevant fields from the API response and maps them
     * to database entity fields. Handles null values safely.
     *
     * @param publication The publication record from Google Scholar API
     * @param researcher The researcher entity this article belongs to
     * @return ArticleEntity ready to be persisted
     */
    public static ArticleEntity toArticleEntity(PublicationRecord publication, ResearcherEntity researcher) {
        ArticleEntity entity = new ArticleEntity();

        entity.setResearcher(researcher);
        entity.setTitle(publication.title());

        // Extract authors as comma-separated string
        if (publication.publicationInfo() != null &&
                publication.publicationInfo().authors() != null) {
            String authors = publication.publicationInfo().authors().stream()
                    .map(AuthorInfo::name)
                    .collect(Collectors.joining(", "));
            entity.setAuthors(authors);
        }

        // Extract publication date from summary
        if (publication.publicationInfo() != null) {
            String summary = publication.publicationInfo().summary();
            String publicationDate = extractPublicationDate(summary);
            entity.setPublicationDate(publicationDate);
        }

        // Use snippet as abstract if no dedicated abstract field
        entity.setAbstractText(publication.snippet());
        entity.setSnippet(publication.snippet());
        entity.setLink(publication.link());

        // Extract citation count
        if (publication.inlineLinks() != null &&
                publication.inlineLinks().citedBy() != null) {
            entity.setCitedBy(publication.inlineLinks().citedBy().total());
        } else {
            entity.setCitedBy(0);
        }

        // Keywords can be extracted from title or abstract (simplified approach)
        entity.setKeywords(extractKeywords(publication.title(), publication.snippet()));

        return entity;
    }

    /**
     * Converts ArticleEntity (from database) to ArticleDTO (for API response).
     *
     * Creates an immutable DTO with researcher information included.
     *
     * @param entity The article entity from database
     * @return ArticleDTO for client consumption
     */
    public static ArticleDTO toArticleDTO(ArticleEntity entity) {
        return new ArticleDTO(
                entity.getId(),
                entity.getResearcher().getId(),
                entity.getResearcher().getName(),
                entity.getTitle(),
                entity.getAuthors(),
                entity.getPublicationDate(),
                entity.getAbstractText(),
                entity.getLink(),
                entity.getKeywords(),
                entity.getCitedBy(),
                entity.getSnippet()
        );
    }

    /**
     * Extracts publication date from Google Scholar summary string.
     *
     * Example input: "A Ng, D Smith - Nature, 2023 - nature.com"
     * Extracts: "2023"
     *
     * @param summary The publication summary string
     * @return Extracted publication date or null
     */
    private static String extractPublicationDate(String summary) {
        if (summary == null || summary.isBlank()) {
            return null;
        }

        // Simple regex to find 4-digit year
        String[] parts = summary.split("-");
        for (String part : parts) {
            String trimmed = part.trim();
            // Check if contains a 4-digit year
            if (trimmed.matches(".*\\b(19|20)\\d{2}\\b.*")) {
                // Extract just the year
                String year = trimmed.replaceAll(".*\\b((19|20)\\d{2})\\b.*", "$1");
                return year;
            }
        }

        return null;
    }

    /**
     * Extracts keywords from title and snippet.
     *
     * Simplified approach: Takes significant words from title.
     * In production, would use NLP techniques or manual tagging.
     *
     * @param title Article title
     * @param snippet Article snippet
     * @return Comma-separated keywords
     */
    private static String extractKeywords(String title, String snippet) {
        if (title == null || title.isBlank()) {
            return null;
        }

        // Remove common words and extract significant terms
        String[] words = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .split("\\s+");

        // Filter out common words (simplified stop words)
        String[] stopWords = {"the", "a", "an", "and", "or", "but", "in", "on", "at",
                "to", "for", "of", "with", "by", "from", "as"};

        StringBuilder keywords = new StringBuilder();
        for (String word : words) {
            boolean isStopWord = false;
            for (String stop : stopWords) {
                if (word.equals(stop)) {
                    isStopWord = true;
                    break;
                }
            }
            if (!isStopWord && word.length() > 3) {
                if (keywords.length() > 0) {
                    keywords.append(", ");
                }
                keywords.append(word);
            }
        }

        return keywords.toString();
    }
}