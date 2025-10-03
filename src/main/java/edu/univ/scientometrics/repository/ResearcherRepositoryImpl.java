package edu.univ.scientometrics.repository;

import edu.univ.scientometrics.entity.ResearcherEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of ResearcherRepository.
 *
 * Handles all database operations for researchers using EntityManager.
 * Follows same patterns as ArticleRepositoryImpl for consistency.
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
@Repository
@Transactional(readOnly = true)
public class ResearcherRepositoryImpl implements ResearcherRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves or updates researcher in database.
     * Uses persist for new entities, merge for existing ones.
     */
    @Override
    @Transactional
    public ResearcherEntity save(ResearcherEntity researcher) {
        if (researcher.getId() == null) {
            entityManager.persist(researcher);
            return researcher;
        } else {
            return entityManager.merge(researcher);
        }
    }

    /**
     * Retrieves all researchers ordered by name.
     */
    @Override
    public List<ResearcherEntity> findAll() {
        String jpql = "SELECT r FROM ResearcherEntity r ORDER BY r.name";
        TypedQuery<ResearcherEntity> query = entityManager.createQuery(jpql, ResearcherEntity.class);
        return query.getResultList();
    }

    /**
     * Finds researcher by primary key.
     */
    @Override
    public Optional<ResearcherEntity> findById(Long id) {
        ResearcherEntity researcher = entityManager.find(ResearcherEntity.class, id);
        return Optional.ofNullable(researcher);
    }

    /**
     * Finds researcher by Google Scholar author ID.
     * Uses JPQL query with parameter binding for security.
     */
    @Override
    public Optional<ResearcherEntity> findByAuthorId(String authorId) {
        String jpql = "SELECT r FROM ResearcherEntity r WHERE r.authorId = :authorId";
        TypedQuery<ResearcherEntity> query = entityManager.createQuery(jpql, ResearcherEntity.class);
        query.setParameter("authorId", authorId);

        List<ResearcherEntity> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Finds researchers by partial name match.
     * Uses LIKE clause for flexible searching.
     */
    @Override
    public List<ResearcherEntity> findByNameContaining(String name) {
        String jpql = "SELECT r FROM ResearcherEntity r WHERE LOWER(r.name) LIKE LOWER(:name)";
        TypedQuery<ResearcherEntity> query = entityManager.createQuery(jpql, ResearcherEntity.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    /**
     * Counts total researchers using aggregate query.
     */
    @Override
    public long count() {
        String jpql = "SELECT COUNT(r) FROM ResearcherEntity r";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    /**
     * Deletes researcher entity.
     * Cascade will automatically delete associated articles.
     */
    @Override
    @Transactional
    public void delete(ResearcherEntity researcher) {
        if (entityManager.contains(researcher)) {
            entityManager.remove(researcher);
        } else {
            entityManager.remove(entityManager.merge(researcher));
        }
    }

    /**
     * Deletes researcher by ID.
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        ResearcherEntity researcher = entityManager.find(ResearcherEntity.class, id);
        if (researcher != null) {
            entityManager.remove(researcher);
        }
    }
}