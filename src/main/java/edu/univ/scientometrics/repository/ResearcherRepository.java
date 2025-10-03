package edu.univ.scientometrics.repository;

import edu.univ.scientometrics.entity.ResearcherEntity;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Researcher database operations.
 *
 * Defines contract for researcher data access following
 * Repository pattern and SOLID principles.
 *
 * SOLID Principles:
 * - Single Responsibility: Only researcher data access
 * - Interface Segregation: Specific to researcher operations
 * - Dependency Inversion: Decouples service from persistence details
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
public interface ResearcherRepository {

    /**
     * Saves or updates a researcher in the database.
     *
     * @param researcher The researcher entity to save
     * @return The saved researcher with ID populated
     */
    ResearcherEntity save(ResearcherEntity researcher);

    /**
     * Retrieves all researchers from database.
     *
     * @return List of all researchers
     */
    List<ResearcherEntity> findAll();

    /**
     * Finds a researcher by primary key.
     *
     * @param id The researcher ID
     * @return Optional with researcher if found
     */
    Optional<ResearcherEntity> findById(Long id);

    /**
     * Finds a researcher by their Google Scholar author ID.
     * Useful for checking if researcher already exists before creating.
     *
     * @param authorId The Google Scholar author ID
     * @return Optional with researcher if found
     */
    Optional<ResearcherEntity> findByAuthorId(String authorId);

    /**
     * Finds researchers by name (partial match).
     *
     * @param name Researcher name to search for
     * @return List of researchers matching the name
     */
    List<ResearcherEntity> findByNameContaining(String name);

    /**
     * Counts total researchers in database.
     *
     * @return Total count
     */
    long count();

    /**
     * Deletes a researcher.
     *
     * @param researcher The researcher to delete
     */
    void delete(ResearcherEntity researcher);

    /**
     * Deletes researcher by ID.
     *
     * @param id The researcher ID
     */
    void deleteById(Long id);
}