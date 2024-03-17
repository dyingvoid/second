package com.example.second.domain.services;

import com.example.second.data.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, User> sessions = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        users.put("a", new User("a", "a"));
    }

    public User createUser(HttpServletRequest request) {
        return new User(
                request.getParameter("name"),
                request.getParameter("password")
        );
    }

    public void addUser(User user) {
        users.put(user.getName(), user);
    }

    public boolean validateUser(User user) {
        return validateFields(user);
    }

    public boolean validateFields(User user) {
        return !user.getName().isBlank() &&
                !user.getPassword().isBlank();
    }

    public User getUserBySessionId(String id) {
        return sessions.get(id);
    }

    public User getUserByName(String name) {
        return users.get(name);
    }

    public void addSession(String id, User user) {
        sessions.put(id, user);
    }

    public void deleteSession(String id) {
        sessions.remove(id);
    }
}
