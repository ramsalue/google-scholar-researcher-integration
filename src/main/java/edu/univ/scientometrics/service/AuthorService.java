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
}
