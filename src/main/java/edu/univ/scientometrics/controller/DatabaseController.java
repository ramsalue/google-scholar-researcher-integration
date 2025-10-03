package edu.univ.scientometrics.controller;

import edu.univ.scientometrics.dto.ArticleDTO;
import edu.univ.scientometrics.entity.ArticleEntity;
import edu.univ.scientometrics.entity.ResearcherEntity;
import edu.univ.scientometrics.model.PublicationRecord;
import edu.univ.scientometrics.repository.ArticleRepository;
import edu.univ.scientometrics.repository.ResearcherRepository;
import edu.univ.scientometrics.service.AuthorServiceImpl;
import edu.univ.scientometrics.util.DataMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for database operations.
 *
 * Provides endpoints to:
 * - Save API results to database
 * - Query stored data
 * - View statistics
 *
 * Sprint 3 Features:
 * - Search and save researcher data
 * - View all saved articles
 * - Get database statistics
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final AuthorServiceImpl authorService;
    private final ArticleRepository articleRepository;
    private final ResearcherRepository researcherRepository;

    /**
     * Constructor with dependency injection.
     */
    public DatabaseController(AuthorServiceImpl authorService,
                              ArticleRepository articleRepository,
                              ResearcherRepository researcherRepository) {
        this.authorService = authorService;
        this.articleRepository = articleRepository;
        this.researcherRepository = researcherRepository;
    }

    /**
     * Searches Google Scholar and saves results to database.
     *
     * Sprint 3 Requirement: Store 2 researchers with 3 articles each
     *
     * Endpoint: POST /api/database/save
     *
     * @param name Researcher name to search
     * @param maxArticles Maximum articles to save (default: 3)
     * @return Confirmation message with saved data count
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> searchAndSave(
            @RequestParam String name,
            @RequestParam(defaultValue = "3") int maxArticles) {

        List<PublicationRecord> publications =
                authorService.searchAndSaveToDatabase(name, maxArticles);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data saved successfully");
        response.put("researcherName", name);
        response.put("articlesSaved", Math.min(publications.size(), maxArticles));
        response.put("articlesFound", publications.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all articles from database.
     *
     * Endpoint: GET /api/database/articles
     *
     * @return List of all articles as DTOs
     */
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleEntity> articles = articleRepository.findAll();

        List<ArticleDTO> dtos = articles.stream()
                .map(DataMapper::toArticleDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves all researchers from database.
     *
     * Endpoint: GET /api/database/researchers
     *
     * @return List of all researchers
     */
    @GetMapping("/researchers")
    public ResponseEntity<List<ResearcherEntity>> getAllResearchers() {
        List<ResearcherEntity> researchers = researcherRepository.findAll();
        return ResponseEntity.ok(researchers);
    }

    /**
     * Gets articles for a specific researcher.
     *
     * Endpoint: GET /api/database/articles/researcher/{id}
     *
     * @param id Researcher ID
     * @return List of articles by that researcher
     */
    @GetMapping("/articles/researcher/{id}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByResearcher(@PathVariable Long id) {
        List<ArticleEntity> articles = articleRepository.findByResearcherId(id);

        List<ArticleDTO> dtos = articles.stream()
                .map(DataMapper::toArticleDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Gets database statistics.
     *
     * Endpoint: GET /api/database/stats
     *
     * @return Statistics about stored data
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalResearchers", researcherRepository.count());
        stats.put("totalArticles", articleRepository.count());

        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearAllData() {
        long deletedArticles = articleRepository.count();
        long deletedResearchers = researcherRepository.count();

        articleRepository.findAll().forEach(articleRepository::delete);
        researcherRepository.findAll().forEach(researcherRepository::delete);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Database cleared successfully");
        response.put("deletedArticles", deletedArticles);
        response.put("deletedResearchers", deletedResearchers);

        return ResponseEntity.ok(response);
    }
}