package com.yu.jangtari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Date Auditing 활성화
@SpringBootApplication
public class JangtariApplication {
    public static void main(String[] args) {
        SpringApplication.run(JangtariApplication.class, args);
    }
}
