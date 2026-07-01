package com.streamwise.streamwise.controller;

import com.streamwise.streamwise.dto.ApiResponse;
import com.streamwise.streamwise.dto.MovieSearchRequest;
import com.streamwise.streamwise.model.SearchHistory;
import com.streamwise.streamwise.service.GroqService;
import com.streamwise.streamwise.service.SearchHistoryService;
import com.streamwise.streamwise.service.TmdbService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private GroqService groqService;

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private SearchHistoryService searchHistoryService;

    // Health check — public
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("StreamWise Backend is running!", "OK")
        );
    }

    // AI Movie Search — protected
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<String>> search(
            @Valid @RequestBody MovieSearchRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();

        // Save search history
        searchHistoryService.saveSearch(userEmail, request.getQuery());

        // Get AI recommendations (cached)
        String recommendations = groqService.getMovieRecommendations(request.getQuery());

        return ResponseEntity.ok(
                ApiResponse.success("Movies fetched successfully", recommendations)
        );
    }

    // TMDB Search — protected
    @GetMapping("/tmdb")
    public ResponseEntity<ApiResponse<String>> tmdbSearch(
            @RequestParam String title) {
        String result = tmdbService.searchMovie(title);
        return ResponseEntity.ok(
                ApiResponse.success("TMDB results fetched", result)
        );
    }

    // Get search history — protected
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<SearchHistory>>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        List<SearchHistory> history =
                searchHistoryService.getSearchHistory(userEmail);
        return ResponseEntity.ok(
                ApiResponse.success("Search history fetched", history)
        );
    }

    // Delete search history — protected
    @DeleteMapping("/history/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHistory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        searchHistoryService.deleteSearch(id, userEmail);
        return ResponseEntity.ok(
                ApiResponse.success("Search history deleted")
        );
    }
}