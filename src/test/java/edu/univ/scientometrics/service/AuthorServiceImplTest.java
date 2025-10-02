package edu.univ.scientometrics.service;

import edu.univ.scientometrics.exception.ApiException;
import edu.univ.scientometrics.model.ApiResponseRecord;
import edu.univ.scientometrics.model.PublicationRecord;
import edu.univ.scientometrics.model.SearchMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private ApiClient apiClient;

    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(apiClient);
    }

    @Test
    void searchByAuthor_ShouldReturnPublications() {
        // Arrange
        PublicationRecord mockPublication = new PublicationRecord(
                "Test Paper",
                "http://example.com",
                null,
                "Test snippet",
                null
        );

        SearchMetadata metadata = new SearchMetadata(
                "test-id",
                "Success",
                "2024-01-01",
                1.5
        );

        ApiResponseRecord mockResponse = new ApiResponseRecord(
                metadata,
                List.of(mockPublication),
                null
        );

        when(apiClient.get(any(Map.class))).thenReturn(mockResponse);

        // Act
        List<PublicationRecord> results = authorService.searchByAuthor("Test Author");

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Paper", results.get(0).title());
        verify(apiClient, times(1)).get(any(Map.class));
    }

    @Test
    void searchByAuthorWithPagination_ShouldValidateParameters() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                authorService.searchByAuthorWithPagination("Test Author", 0, 0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                authorService.searchByAuthorWithPagination("Test Author", 0, 25)
        );
    }

    @Test
    void searchByAuthor_ShouldThrowException_WhenApiReturnsError() {
        // Arrange
        ApiResponseRecord errorResponse = new ApiResponseRecord(
                null,
                null,
                "API Error occurred"
        );

        when(apiClient.get(any(Map.class))).thenReturn(errorResponse);

        // Act & Assert
        assertThrows(ApiException.class, () ->
                authorService.searchByAuthor("Test Author")
        );
    }
}