// src/main/java/com/collage/inventory/Repository/UserRepository.java
package com.collage.inventory.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.collage.inventory.Entity.User;

public interface UserRepository extends JpaRepository<User, String> {}
