package com.union.demo.dto.response;

import java.util.List;

public class PostMatchResDto {
    private List<PostMatchUserDto> results;

    public PostMatchResDto(){};

    public List<PostMatchUserDto> getResults() {
        return results;
    }

    public void setResults(List<PostMatchUserDto> results) {
        this.results = results;
    }
}
