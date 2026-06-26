package com.streamwise.streamwise.controller;

import com.streamwise.streamwise.service.GroqService;
import com.streamwise.streamwise.service.SearchHistoryService;
import com.streamwise.streamwise.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private GroqService groqService;

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private SearchHistoryService searchHistoryService;

    // Health check
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("StreamWise Backend is running!");
    }

    // GPT Search — calls Groq AI
    @PostMapping("/search")
    public ResponseEntity<String> search(@RequestBody Map<String, String> body) {
        String query = body.get("query");
        String userEmail = body.getOrDefault("userEmail", "anonymous");

        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().body("Query cannot be empty");
        }

        // Save search to PostgreSQL
        searchHistoryService.saveSearch(userEmail, query);

        // Get AI recommendations from Groq
        String groqResponse = groqService.getMovieRecommendations(query);

        return ResponseEntity.ok(groqResponse);
    }

    // TMDB movie search — fetch poster + rating
    @GetMapping("/tmdb")
    public ResponseEntity<String> tmdbSearch(@RequestParam String title) {
        String result = tmdbService.searchMovie(title);
        return ResponseEntity.ok(result);
    }

    // Get search history for a user
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestParam String userEmail) {
        return ResponseEntity.ok(searchHistoryService.getSearchHistory(userEmail));
    }
}