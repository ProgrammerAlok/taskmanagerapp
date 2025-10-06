package com.example.taskmanagerapp.repository;

import com.example.taskmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*
        SELECT id, name, email 
        FROM users 
        WHERE email = ?;
    */ 
    Optional<User> findByEmail(String email);
}
