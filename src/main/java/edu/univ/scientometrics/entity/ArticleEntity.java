package edu.univ.scientometrics.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing an academic article in the database.
 *
 * This class maps to the 'articles' table and stores publication information
 * retrieved from Google Scholar API. Unlike Records, this is a mutable class
 * to comply with JPA requirements.
 *
 * SOLID Principles:
 * - Single Responsibility: Only represents article data and database mapping
 * - Open/Closed: Can be extended without modification through JPA inheritance
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
@Entity
@Table(name = "articles", indexes = {
        @Index(name = "idx_researcher_id", columnList = "researcher_id"),
        @Index(name = "idx_title", columnList = "title")
})
public class ArticleEntity {

    /**
     * Primary key - auto-generated unique identifier for each article
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Foreign key reference to the researcher who authored this article.
     * Many articles can belong to one researcher (Many-to-One relationship)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "researcher_id", nullable = false)
    private ResearcherEntity researcher;

    /**
     * Article title - limited to 500 characters
     */
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    /**
     * Comma-separated list of all authors
     * Stored as TEXT for unlimited length
     */
    @Column(name = "authors", columnDefinition = "TEXT")
    private String authors;

    /**
     * Publication date as string (format varies by source)
     * Examples: "2023", "Jan 2023", "2023-01-15"
     */
    @Column(name = "publication_date", length = 50)
    private String publicationDate;

    /**
     * Article abstract/summary
     * Stored as TEXT for long content
     */
    @Column(name = "abstract", columnDefinition = "TEXT")
    private String abstractText;

    /**
     * URL link to the full article
     */
    @Column(name = "link", length = 500)
    private String link;

    /**
     * Comma-separated keywords/tags
     */
    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

    /**
     * Number of citations this article has received
     */
    @Column(name = "cited_by")
    private Integer citedBy;

    /**
     * Short description/snippet from Google Scholar
     */
    @Column(name = "snippet", columnDefinition = "TEXT")
    private String snippet;

    /**
     * Timestamp when this record was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when this record was last updated
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ============================================
    // CONSTRUCTORS
    // ============================================

    /**
     * Default constructor required by JPA
     */
    public ArticleEntity() {
        // JPA requires no-arg constructor
    }

    /**
     * Constructor with all fields except timestamps (auto-generated)
     */
    public ArticleEntity(ResearcherEntity researcher, String title, String authors,
                         String publicationDate, String abstractText, String link,
                         String keywords, Integer citedBy, String snippet) {
        this.researcher = researcher;
        this.title = title;
        this.authors = authors;
        this.publicationDate = publicationDate;
        this.abstractText = abstractText;
        this.link = link;
        this.keywords = keywords;
        this.citedBy = citedBy;
        this.snippet = snippet;
    }

    // ============================================
    // JPA LIFECYCLE CALLBACKS
    // ============================================

    /**
     * Called before persisting the entity to database
     * Sets the createdAt timestamp
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Called before updating the entity in database
     * Updates the updatedAt timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ============================================
    // GETTERS AND SETTERS
    // Required by JPA for field access
    // ============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResearcherEntity getResearcher() {
        return researcher;
    }

    public void setResearcher(ResearcherEntity researcher) {
        this.researcher = researcher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getCitedBy() {
        return citedBy;
    }

    public void setCitedBy(Integer citedBy) {
        this.citedBy = citedBy;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
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
        return "ArticleEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", citedBy=" + citedBy +
                ", publicationDate='" + publicationDate + '\'' +
                '}';
    }
}