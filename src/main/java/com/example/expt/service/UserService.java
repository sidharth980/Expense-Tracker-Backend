package com.example.expt.service;

import com.example.expt.entity.User;

public interface UserService {
    User getUserById(Long userId);
    User getOtherUser(Long currentUserId);
}
