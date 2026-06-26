package com.streamwise.streamwise.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TmdbService {

    @Value("${tmdb.bearer.token}")
    private String bearerToken;

    @Value("${tmdb.api.url}")
    private String tmdbApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchMovie(String movieTitle) {
        String url = tmdbApiUrl + "/search/movie?query=" +
                movieTitle.replace(" ", "%20") +
                "&include_adult=false&language=en-US&page=1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.set("accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}