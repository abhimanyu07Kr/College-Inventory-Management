package com.collage.inventory.Controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping("/api/user-role")
    public Map<String, String> getUserRole(Authentication auth) {
        return Map.of("role", auth.getAuthorities().iterator().next().getAuthority());
    }
}
