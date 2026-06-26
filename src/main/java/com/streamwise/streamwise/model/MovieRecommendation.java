package com.streamwise.streamwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRecommendation {
    private String title;
    private int year;
    private String genre;
    private String reason;
    private String castHighlight;
    private String moodTag;


}
