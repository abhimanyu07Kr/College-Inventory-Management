package com.collage.inventory.Controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collage.inventory.Entity.User;
import com.collage.inventory.Repository.UserRepository;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
	private final UserRepository userRepository;

	public AdminController(UserRepository users) {
		this.userRepository = users;
	}


	
	 @PostMapping("/create-subadmin")
	    public ResponseEntity<?> createSubAdmin(@RequestParam String username, @RequestParam String password) {
	        if (userRepository.existsById(username)) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
	        }
	        User user = new User();
	        user.setUsername(username);
	        user.setPassword(new BCryptPasswordEncoder().encode(password));
	        user.setRoles(Set.of("ROLE_SUBADMIN"));
	        userRepository.save(user);
	        return ResponseEntity.ok("Sub-admin created");
	    }

	    @GetMapping("/subadmins")
	    public List<User> getAllSubAdmins() {
	        List<User> collect = userRepository.findAll().stream()
	                .filter(user -> user.getRoles().contains("ROLE_SUBADMIN"))
	                .collect(Collectors.toList());
	        return collect;
	    }

	    @PutMapping("/update-subadmin")
	    public ResponseEntity<?> updateSubAdmin(@RequestParam String username, @RequestParam String password) {
	        Optional<User> existing = userRepository.findById(username);
	        if (existing.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sub-admin not found");
	        }
	        User user = existing.get();
	        user.setPassword(new BCryptPasswordEncoder().encode(password));
	        System.out.println(user.getUsername()+"  "+user.getPassword());
	        userRepository.save(user);
	        return ResponseEntity.ok("Password updated");
	    }

	    @DeleteMapping("/delete-subadmin/{username}")
	    public ResponseEntity<?> deleteSubAdmin(@PathVariable String username) {
	        Optional<User> userOpt = userRepository.findById(username);
	        if (userOpt.isEmpty() || !userOpt.get().getRoles().contains("ROLE_SUBADMIN")) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sub-admin not found");
	        }
	        userRepository.deleteById(username);
	        return ResponseEntity.ok("Sub-admin deleted");
	    }
}
