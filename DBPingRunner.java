package com.union.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbPingRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        System.out.println("✅ DB 연결 확인: SELECT 1 -> " + one);
    }
}
