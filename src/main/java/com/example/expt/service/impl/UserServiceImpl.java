package com.example.expt.service.impl;

import com.example.expt.entity.User;
import com.example.expt.repository.UserRepository;
import com.example.expt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getOtherUser(Long currentUserId) {
        // Logic to fetch another user, excluding the current user.
        // This is a placeholder implementation.  A more robust solution
        // might involve a query that excludes the current user ID.
        return userRepository.findAll().stream()
                .filter(user -> !user.getUserId().equals(currentUserId))
                .findFirst()
                .orElse(null);
    }
}
