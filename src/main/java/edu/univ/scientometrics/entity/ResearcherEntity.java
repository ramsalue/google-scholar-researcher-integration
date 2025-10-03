package edu.univ.scientometrics.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing an academic researcher in the database.
 *
 * This class maps to the 'researchers' table and stores author information
 * from Google Scholar. Maintains a one-to-many relationship with articles.
 *
 * SOLID Principles:
 * - Single Responsibility: Only represents researcher data and relationships
 * - Open/Closed: Extensible through JPA features
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
@Entity
@Table(name = "researchers", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_author_id", columnList = "author_id")
})
public class ResearcherEntity {

    /**
     * Primary key - auto-generated unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Researcher's full name
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Google Scholar author ID (unique identifier from API)
     * Example: "mG4imMEAAAAJ"
     */
    @Column(name = "author_id", unique = true, length = 100)
    private String authorId;

    /**
     * Academic affiliations/institutions
     */
    @Column(name = "affiliations", columnDefinition = "TEXT")
    private String affiliations;

    /**
     * Total number of citations across all publications
     */
    @Column(name = "cited_by")
    private Integer citedBy;

    /**
     * Email address (if available)
     */
    @Column(name = "email")
    private String email;

    /**
     * List of articles authored by this researcher
     * One researcher can have many articles (One-to-Many relationship)
     * CascadeType.ALL: Operations on researcher cascade to articles
     * orphanRemoval: Deleting researcher removes associated articles
     */
    @OneToMany(mappedBy = "researcher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleEntity> articles = new ArrayList<>();

    /**
     * Record creation timestamp
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Record last update timestamp
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    /**
     * Default constructor required by JPA
     */
    public ResearcherEntity() {
        // Required by JPA
    }

    /**
     * Constructor with essential fields
     */
    public ResearcherEntity(String name, String authorId, String affiliations, Integer citedBy) {
        this.name = name;
        this.authorId = authorId;
        this.affiliations = affiliations;
        this.citedBy = citedBy;
    }

    // ============================================
    // JPA LIFECYCLE CALLBACKS
    // ============================================

    /**
     * Called before persisting to database
     * Sets creation timestamp
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Called before updating in database
     * Updates modification timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ============================================
    // RELATIONSHIP HELPER METHODS
    // ============================================

    /**
     * Adds an article to this researcher's publication list
     * Maintains bidirectional relationship consistency
     *
     * @param article The article to add
     */
    public void addArticle(ArticleEntity article) {
        articles.add(article);
        article.setResearcher(this);
    }

    /**
     * Removes an article from this researcher's publication list
     * Maintains bidirectional relationship consistency
     *
     * @param article The article to remove
     */
    public void removeArticle(ArticleEntity article) {
        articles.remove(article);
        article.setResearcher(null);
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(String affiliations) {
        this.affiliations = affiliations;
    }

    public Integer getCitedBy() {
        return citedBy;
    }

    public void setCitedBy(Integer citedBy) {
        this.citedBy = citedBy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ArticleEntity> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleEntity> articles) {
        this.articles = articles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ============================================
    // UTILITY METHODS
    // ============================================

    @Override
    public String toString() {
        return "ResearcherEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorId='" + authorId + '\'' +
                ", citedBy=" + citedBy +
                ", articlesCount=" + articles.size() +
                '}';
    }
}