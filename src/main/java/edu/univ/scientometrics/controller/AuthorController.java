package edu.univ.scientometrics.controller;

import edu.univ.scientometrics.model.PublicationRecord;
import edu.univ.scientometrics.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<PublicationRecord>> searchAuthor(
            @RequestParam String name
    ) {
        List<PublicationRecord> publications = authorService.searchByAuthor(name);
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<List<PublicationRecord>> searchAuthorPaginated(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int num
    ) {
        List<PublicationRecord> publications =
                authorService.searchByAuthorWithPagination(name, start, num);
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/search/date-range")
    public ResponseEntity<List<PublicationRecord>> searchAuthorDateRange(
            @RequestParam String name,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo
    ) {
        List<PublicationRecord> publications =
                authorService.searchByAuthorWithDateRange(name, yearFrom, yearTo);
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Google Scholar Integration API is running");
    }
}
