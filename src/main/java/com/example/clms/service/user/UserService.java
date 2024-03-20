package com.example.clms.service.user;

import com.example.clms.entity.user.User;

public interface UserService {
    User getUser(String email);
    void deleteUser(String email);
    void resetPassword(String username, String password);
}
