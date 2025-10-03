package edu.univ.scientometrics.service;

import edu.univ.scientometrics.entity.ArticleEntity;
import edu.univ.scientometrics.entity.ResearcherEntity;
import edu.univ.scientometrics.exception.ApiException;
import edu.univ.scientometrics.exception.DatabaseException;
import edu.univ.scientometrics.model.ApiResponseRecord;
import edu.univ.scientometrics.model.PublicationRecord;
import edu.univ.scientometrics.repository.ArticleRepository;
import edu.univ.scientometrics.repository.ResearcherRepository;
import edu.univ.scientometrics.util.DataMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final ApiClient apiClient;
    private final ArticleRepository articleRepository;
    private final ResearcherRepository researcherRepository;

    public AuthorServiceImpl(ApiClient apiClient,
                             ArticleRepository articleRepository,
                             ResearcherRepository researcherRepository) {
        this.apiClient = apiClient;
        this.articleRepository = articleRepository;
        this.researcherRepository = researcherRepository;
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

    @Override
    @Transactional
    public List<PublicationRecord> searchAndSaveToDatabase(String authorName, int maxArticles) {
        List<PublicationRecord> publications = searchByAuthor(authorName);

        if (publications.isEmpty()) {
            return publications;
        }

        try {
            ResearcherEntity researcher = findOrCreateResearcher(authorName, publications);

            List<ArticleEntity> existingArticles = articleRepository.findByResearcherId(researcher.getId());

            int articlesToSave = Math.min(publications.size(), maxArticles);
            int savedCount = 0;

            for (int i = 0; i < articlesToSave && savedCount < maxArticles; i++) {
                PublicationRecord publication = publications.get(i);

                boolean alreadyExists = existingArticles.stream()
                        .anyMatch(existing -> existing.getTitle().equals(publication.title()));

                if (!alreadyExists) {
                    ArticleEntity article = DataMapper.toArticleEntity(publication, researcher);
                    articleRepository.save(article);
                    savedCount++;
                }
            }

            return publications;

        } catch (Exception e) {
            throw new DatabaseException(
                    "Failed to save articles to database: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Finds an existing researcher or creates a new one.
     *
     * Checks the database for a researcher by name. If not found,
     * it creates a new researcher entity using the author name and
     * extracts additional information (like an authorId) from the
     * list of publications before saving.
     *
     * @param authorName Author name to search for
     * @param publications Publications used to extract author info if creating a new researcher
     * @return Researcher entity (existing or newly created)
     */
    private ResearcherEntity findOrCreateResearcher(String authorName, List<PublicationRecord> publications) {
        // Try to find an existing researcher by name (using the Repository)
        List<ResearcherEntity> existing = researcherRepository.findByNameContaining(authorName);

        if (!existing.isEmpty()) {
            // Researcher found, return the first result
            return existing.get(0);
        }

        // Researcher not found, proceed to create a new one
        ResearcherEntity researcher = new ResearcherEntity();
        researcher.setName(authorName);

        // Extract additional info from the first publication, if available
        if (!publications.isEmpty() && publications.get(0).publicationInfo() != null) {
            var pubInfo = publications.get(0).publicationInfo();

            if (pubInfo.authors() != null && !pubInfo.authors().isEmpty()) {
                // Attempt to get the authorId from the first author listed
                var firstAuthor = pubInfo.authors().get(0);
                researcher.setAuthorId(firstAuthor.authorId());
            }
        }

        // Save the new researcher entity to the database and return it
        return researcherRepository.save(researcher);
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