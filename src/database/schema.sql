-- ============================================
-- Google Scholar Integration Database Schema
-- Sprint 3: Database Integration
-- ============================================

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS scholar_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE scholar_db;

-- ============================================
-- Table: researchers
-- Stores information about academic researchers
-- ============================================
CREATE TABLE IF NOT EXISTS researchers (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           name VARCHAR(255) NOT NULL,
    author_id VARCHAR(100) UNIQUE,
    affiliations TEXT,
    cited_by INT DEFAULT 0,
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_name (name),
    INDEX idx_author_id (author_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Table: articles
-- Stores academic publications/articles
-- ============================================
CREATE TABLE IF NOT EXISTS articles (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        researcher_id BIGINT NOT NULL,
                                        title VARCHAR(500) NOT NULL,
    authors TEXT,
    publication_date VARCHAR(50),
    abstract TEXT,
    link VARCHAR(500),
    keywords TEXT,
    cited_by INT DEFAULT 0,
    snippet TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (researcher_id) REFERENCES researchers(id) ON DELETE CASCADE,
    INDEX idx_researcher_id (researcher_id),
    INDEX idx_title (title(255))
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Queries for Testing
-- ============================================

-- View all researchers
-- SELECT * FROM researchers;

-- View all articles with researcher name
-- SELECT
--     a.id, a.title, a.cited_by, r.name as researcher_name
-- FROM articles a
-- JOIN researchers r ON a.researcher_id = r.id
-- ORDER BY a.cited_by DESC;

-- Count articles per researcher
-- SELECT
--     r.name, COUNT(a.id) as article_count
-- FROM researchers r
-- LEFT JOIN articles a ON r.id = a.researcher_id
-- GROUP BY r.id, r.name;