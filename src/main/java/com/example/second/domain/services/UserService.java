package com.example.second.domain.services;

import com.example.second.data.User;
import com.example.second.data.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private volatile static UserService instance = null;
    private static final Object mutex = new Object();

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null)
                    instance = new UserService();
            }
        }

        return instance;
    }

    public User createUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        if (!validateFields(name, password))
            throw new IllegalArgumentException("Can't create user with null fields");

        return new User(name, password);
    }

    public boolean validateFields(String name, String password) {
        return name != null && password != null &&
                !name.isBlank() && !password.isBlank();
    }
}
