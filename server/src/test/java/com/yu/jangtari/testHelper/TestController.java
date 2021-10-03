package com.yu.jangtari.testHelper;

import com.yu.jangtari.security.jwt.JwtInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
public class TestController {
    @GetMapping("/admin/test")
    public JwtInfo adminTest() {
        return getReturnValue();
    }

    @GetMapping("/user/test")
    public JwtInfo userTest() {
        return getReturnValue();
    }

    private JwtInfo getReturnValue() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtInfo) authentication.getPrincipal();
    }
}
