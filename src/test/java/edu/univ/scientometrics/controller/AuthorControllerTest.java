package edu.univ.scientometrics.controller;

import edu.univ.scientometrics.model.PublicationRecord;
import edu.univ.scientometrics.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void healthEndpoint_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/authors/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Google Scholar Integration API is running"));
    }

    @Test
    void searchAuthor_ShouldReturnPublications() throws Exception {
        // Arrange
        PublicationRecord mockPublication = new PublicationRecord(
                "Test Paper",
                "http://example.com",
                null,
                "Test snippet",
                null
        );

        when(authorService.searchByAuthor(anyString()))
                .thenReturn(List.of(mockPublication));

        // Act & Assert
        mockMvc.perform(get("/api/authors/search")
                        .param("name", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Paper"))
                .andExpect(jsonPath("$[0].link").value("http://example.com"));
    }

    @Test
    void searchAuthorPaginated_ShouldAcceptPaginationParams() throws Exception {
        when(authorService.searchByAuthorWithPagination(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/authors/search/paginated")
                        .param("name", "Test Author")
                        .param("start", "10")
                        .param("num", "5"))
                .andExpect(status().isOk());
    }
}