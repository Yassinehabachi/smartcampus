package com.example.demo.service;

import com.example.demo.model.User;

public class AuthService {

    public boolean authenticate(User user, String email, String password) {
        return user != null
                && user.isActive()
                && email != null
                && password != null
                && email.equals(user.getEmail())
                && password.equals(user.getPassword());
    }
}
