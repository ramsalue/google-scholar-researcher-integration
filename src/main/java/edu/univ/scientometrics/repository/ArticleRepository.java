package edu.univ.scientometrics.repository;

import edu.univ.scientometrics.entity.ArticleEntity;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Article database operations.
 *
 * This interface defines the contract for article data access.
 * Implementations handle the actual database interactions.
 *
 * SOLID Principles:
 * - Single Responsibility: Only defines article data access operations
 * - Interface Segregation: Specific to article operations (not a generic repo)
 * - Dependency Inversion: Services depend on this interface, not implementations
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
public interface ArticleRepository {

    /**
     * Persists a new article or updates an existing one in the database.
     *
     * @param article The article entity to save
     * @return The saved article with generated ID (if new)
     * @throws org.springframework.dao.DataAccessException if database error occurs
     */
    ArticleEntity save(ArticleEntity article);

    /**
     * Retrieves all articles from the database.
     *
     * @return List of all articles (empty list if none found)
     */
    List<ArticleEntity> findAll();

    /**
     * Finds an article by its unique identifier.
     *
     * @param id The article ID to search for
     * @return Optional containing the article if found, empty Optional otherwise
     */
    Optional<ArticleEntity> findById(Long id);

    /**
     * Finds all articles authored by a specific researcher.
     *
     * @param researcherId The researcher's ID
     * @return List of articles by the researcher (empty if none)
     */
    List<ArticleEntity> findByResearcherId(Long researcherId);

    /**
     * Counts total number of articles in database.
     *
     * @return Total article count
     */
    long count();

    /**
     * Deletes an article from the database.
     *
     * @param article The article entity to delete
     */
    void delete(ArticleEntity article);

    /**
     * Deletes an article by its ID.
     *
     * @param id The ID of the article to delete
     */
    void deleteById(Long id);
}