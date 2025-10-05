package com.example.taskmanagerapp.service;

import com.example.taskmanagerapp.entity.User;
import com.example.taskmanagerapp.exception.BadRequestException;
import com.example.taskmanagerapp.exception.ResourceNotFoundException;
import com.example.taskmanagerapp.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public User     createUser(String name, String email) {
        if (userRepo.findByEmail(email).isPresent()) {
            throw new BadRequestException("email_already_exists");
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return userRepo.save(u);
    }

    public User getUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
