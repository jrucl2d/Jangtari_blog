package com.yu.jangtari.testHelper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/admin/test")
    public String adminTest() {
        return getReturnValue();
    }

    @GetMapping("/user/test")
    public String userTest() {
        return getReturnValue();
    }

    private String getReturnValue() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = String.valueOf(authentication.getPrincipal());
        String role = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse(null);
        return userId + role;
    }
}
