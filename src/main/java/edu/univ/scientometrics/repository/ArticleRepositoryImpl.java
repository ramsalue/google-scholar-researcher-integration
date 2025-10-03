package edu.univ.scientometrics.repository;

import edu.univ.scientometrics.entity.ArticleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of ArticleRepository interface.
 *
 * This class handles all database operations for articles using
 * JPA EntityManager. All write operations are wrapped in transactions.
 *
 * SOLID Principles:
 * - Single Responsibility: Only handles article database persistence
 * - Open/Closed: Implements interface, can be replaced with other implementations
 * - Liskov Substitution: Can substitute any ArticleRepository implementation
 * - Dependency Inversion: Depends on EntityManager abstraction
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
@Repository
@Transactional(readOnly = true) // Default: read-only transactions for queries
public class ArticleRepositoryImpl implements ArticleRepository {

    /**
     * JPA EntityManager injected by Spring
     * Handles all persistence operations
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves or updates an article in the database.
     *
     * If the article has no ID (new entity), it's persisted.
     * If it has an ID (existing entity), it's merged (updated).
     *
     * @param article The article to save
     * @return The managed article entity with ID populated
     */
    @Override
    @Transactional // Write operation needs writable transaction
    public ArticleEntity save(ArticleEntity article) {
        if (article.getId() == null) {
            // New entity - persist it
            entityManager.persist(article);
            return article;
        } else {
            // Existing entity - merge changes
            return entityManager.merge(article);
        }
    }

    /**
     * Retrieves all articles using JPQL query.
     *
     * @return List of all articles ordered by citation count (descending)
     */
    @Override
    public List<ArticleEntity> findAll() {
        String jpql = "SELECT a FROM ArticleEntity a ORDER BY a.citedBy DESC";
        TypedQuery<ArticleEntity> query = entityManager.createQuery(jpql, ArticleEntity.class);
        return query.getResultList();
    }

    /**
     * Finds a single article by its primary key.
     *
     * @param id The article ID
     * @return Optional with article if found, empty otherwise
     */
    @Override
    public Optional<ArticleEntity> findById(Long id) {
        ArticleEntity article = entityManager.find(ArticleEntity.class, id);
        return Optional.ofNullable(article);
    }

    /**
     * Finds all articles for a specific researcher.
     * Uses JPQL with parameter binding to prevent SQL injection.
     *
     * @param researcherId The researcher's ID
     * @return List of articles (empty if none found)
     */
    @Override
    public List<ArticleEntity> findByResearcherId(Long researcherId) {
        String jpql = "SELECT a FROM ArticleEntity a WHERE a.researcher.id = :researcherId " +
                "ORDER BY a.citedBy DESC";
        TypedQuery<ArticleEntity> query = entityManager.createQuery(jpql, ArticleEntity.class);
        query.setParameter("researcherId", researcherId);
        return query.getResultList();
    }

    /**
     * Counts total articles in database using aggregate query.
     *
     * @return Total number of articles
     */
    @Override
    public long count() {
        String jpql = "SELECT COUNT(a) FROM ArticleEntity a";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    /**
     * Deletes an article entity from database.
     * Entity must be managed (retrieved from database) before deletion.
     *
     * @param article The article to delete
     */
    @Override
    @Transactional
    public void delete(ArticleEntity article) {
        // Ensure entity is managed before deletion
        if (entityManager.contains(article)) {
            entityManager.remove(article);
        } else {
            // If detached, merge first then remove
            entityManager.remove(entityManager.merge(article));
        }
    }

    /**
     * Deletes an article by its ID.
     * More efficient than delete(entity) when you only have the ID.
     *
     * @param id The article ID to delete
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        ArticleEntity article = entityManager.find(ArticleEntity.class, id);
        if (article != null) {
            entityManager.remove(article);
        }
    }
}