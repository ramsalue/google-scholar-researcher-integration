package edu.univ.scientometrics.service;

import edu.univ.scientometrics.model.PublicationRecord;
import java.util.List;

public interface AuthorService {
    List<PublicationRecord> searchByAuthor(String authorName);

    List<PublicationRecord> searchByAuthorWithPagination(
            String authorName,
            int start,
            int numResults
    );

    List<PublicationRecord> searchByAuthorWithDateRange(
            String authorName,
            Integer yearFrom,
            Integer yearTo
    );

    /**
     * Searches for author publications and saves them to database.
     *
     * @param authorName Name of the author to search
     * @param maxArticles Maximum number of articles to save
     * @return List of publications from API
     */
    List<PublicationRecord> searchAndSaveToDatabase(String authorName, int maxArticles);
}
