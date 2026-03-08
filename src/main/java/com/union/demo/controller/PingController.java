package com.union.demo.controller;

import com.union.demo.dto.response.PingResDto;
import com.union.demo.service.PingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//fast api 연결이 잘 되는지 확인하는 controller
//http://localhost:8080/test-python?postId=123
@RestController
public class PingController {

    private final PingService pingService;

    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("/test-python")
    public PingResDto testPython(@RequestParam(defaultValue = "123") Long postId) {
        return pingService.pingFastApi(postId);
    }
}

