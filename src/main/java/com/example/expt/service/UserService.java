package com.example.expt.service;

import com.example.expt.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    List<User> getAllUsers();
}
