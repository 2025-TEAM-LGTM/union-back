package com.union.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class VectorService {

    private final RestTemplate restTemplate;

    public VectorService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public void vectorizePost(Long postId){
        String url = "http://localhost:8000/vectorize/post";

        Map<String, Object> req = Map.of(
                "post_id", postId
        );
        restTemplate.postForObject(url,req,Void.class);
    }

    public void vectorizePortfolio(Long portfolioId){
        String url = "http://localhost:8000/vectorize/portfolio";
        Map<String, Object> req = Map.of(
                "portfolio_id", portfolioId
        );

        restTemplate.postForObject(url,req,Void.class);
    }
}
