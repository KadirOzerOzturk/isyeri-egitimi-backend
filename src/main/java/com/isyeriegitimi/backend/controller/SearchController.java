package com.isyeriegitimi.backend.controller;

import com.isyeriegitimi.backend.dto.SearchResultDto;
import com.isyeriegitimi.backend.model.ApiResponse;
import com.isyeriegitimi.backend.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SearchResultDto>>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(searchService.searchByName(name), "Search result"));
    }
}
