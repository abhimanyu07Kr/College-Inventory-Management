package com.collage.inventory.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.collage.inventory.Entity.User;
import com.collage.inventory.Repository.UserRepository;

@Configuration
public class InitAdmin {
	
	@Bean
    CommandLineRunner run(UserRepository repo, PasswordEncoder enc) {
        return args -> {
            if (!repo.existsById("admin")) {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(enc.encode("admin123"));
                u.setRoles(Set.of("ROLE_ADMIN"));
                repo.save(u);
            }
        };
    }

}
