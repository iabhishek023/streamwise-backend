package com.streamwise.streamwise.service;

import com.streamwise.streamwise.model.SearchHistory;
import com.streamwise.streamwise.repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchHistoryService {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    public void saveSearch(String userEmail, String query) {
        SearchHistory history = new SearchHistory();
        history.setUserEmail(userEmail);
        history.setQuery(query);
        searchHistoryRepository.save(history);
    }

    public List<SearchHistory> getSearchHistory(String userEmail) {
        return searchHistoryRepository.findByUserEmailOrderBySearchedAtDesc(userEmail);
    }
}