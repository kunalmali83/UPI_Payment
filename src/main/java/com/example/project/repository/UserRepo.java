package com.example.project.repository;


import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;


import com.example.project.entities.User;
public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findById(String userId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

