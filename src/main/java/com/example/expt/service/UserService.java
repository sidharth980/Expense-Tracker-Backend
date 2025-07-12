package com.example.expt.service;

import com.example.expt.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    List<User> getAllUsers();
    
    // Authentication methods
    User findByUsername(String username);
    boolean existsByUsername(String username);
    User createUser(String username, String password);
}
