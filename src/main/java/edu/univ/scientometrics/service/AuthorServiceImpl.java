package edu.univ.scientometrics.service;

import edu.univ.scientometrics.exception.ApiException;
import edu.univ.scientometrics.model.ApiResponseRecord;
import edu.univ.scientometrics.model.PublicationRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final ApiClient apiClient;

    public AuthorServiceImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<PublicationRecord> searchByAuthor(String authorName) {
        Map<String, String> params = new HashMap<>();
        params.put("q", "author:\"" + authorName + "\"");
        return executeSearch(params);
    }

    @Override
    public List<PublicationRecord> searchByAuthorWithPagination(
            String authorName,
            int start,
            int numResults
    ) {
        if (numResults < 1 || numResults > 20) {
            throw new IllegalArgumentException(
                    "numResults must be between 1 and 20"
            );
        }

        Map<String, String> params = new HashMap<>();
        params.put("q", "author:\"" + authorName + "\"");
        params.put("start", String.valueOf(start));
        params.put("num", String.valueOf(numResults));

        return executeSearch(params);
    }

    @Override
    public List<PublicationRecord> searchByAuthorWithDateRange(
            String authorName,
            Integer yearFrom,
            Integer yearTo
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("q", "author:\"" + authorName + "\"");

        if (yearFrom != null) {
            params.put("as_ylo", yearFrom.toString());
        }
        if (yearTo != null) {
            params.put("as_yhi", yearTo.toString());
        }

        return executeSearch(params);
    }

    private List<PublicationRecord> executeSearch(Map<String, String> params) {
        ApiResponseRecord response = apiClient.get(params);

        if (response.error() != null) {
            throw new ApiException(
                    "API returned error: " + response.error(),
                    500
            );
        }

        if (response.searchMetadata() != null
                && !"Success".equals(response.searchMetadata().status())) {
            throw new ApiException(
                    "Search failed with status: " + response.searchMetadata().status(),
                    500
            );
        }

        return response.organicResults() != null
                ? response.organicResults()
                : List.of();
    }
}
