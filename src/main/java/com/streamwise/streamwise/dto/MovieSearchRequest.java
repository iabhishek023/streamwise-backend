package com.streamwise.streamwise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovieSearchRequest {

    @NotBlank(message = "Search query is required")
    @Size(min = 3, max = 500, message = "Query must be between 3 and 500 characters")
    private String query;

    private String userEmail;
}