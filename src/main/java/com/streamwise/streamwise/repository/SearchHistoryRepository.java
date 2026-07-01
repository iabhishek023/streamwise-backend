package com.streamwise.streamwise.repository;

import com.streamwise.streamwise.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserEmailOrderBySearchedAtDesc(String userEmail);
    void deleteByIdAndUserEmail(Long id, String userEmail);
}