package com.yu.jangtari;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController
{
    @GetMapping("/admin/test")
    @ResponseStatus(HttpStatus.OK)
    public String adminTest() {
        return "admin";
    }

    @GetMapping("/user/test")
    @ResponseStatus(HttpStatus.OK)
    public String userTest() {
        return "user";
    }
}
