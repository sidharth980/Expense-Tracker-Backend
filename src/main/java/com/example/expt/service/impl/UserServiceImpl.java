package com.example.expt.service.impl;

import com.example.expt.entity.User;
import com.example.expt.repository.UserRepository;
import com.example.expt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
    return userRepository.findAll();
    }
}
