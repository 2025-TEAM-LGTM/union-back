package com.union.demo.controller;

import com.union.demo.dto.response.PingResDto;
import com.union.demo.service.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
@RequiredArgsConstructor
public class PingController {

    private final PingService pingService;

    @GetMapping("/python")
    public ResponseEntity<PingResDto> pingPython(
            @RequestParam(name = "postId", defaultValue = "123") Long postId
    ) {
        PingResDto response = pingService.pingFastApi(postId);

        return ResponseEntity.ok(response);
    }
}