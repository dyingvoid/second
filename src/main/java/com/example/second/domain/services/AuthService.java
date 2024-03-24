package com.example.second.domain.services;

import com.example.second.GlobalManager;
import com.example.second.data.User;
import com.example.second.data.UserRepository;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthService {
    private static volatile AuthService instance = null;
    private static final Object mutex = new Object();

    private Map<String, User> sessions;
    private UserRepository userRepository;
    private UserService userService;

    private AuthService() {
        sessions = new HashMap<>();
        userRepository = UserRepository.getInstance();
        userService = GlobalManager.userService;
    }

    public static AuthService getInstance() {
        if(instance == null){
            synchronized (mutex) {
                if(instance == null) {
                    instance = new AuthService();
                }
            }
        }

        return instance;
    }

    public boolean register(User user) throws SQLException {
        User found = userRepository.findUserByName(user.getName());

        if(found != null)
            return false;

        userRepository.addUser(user);
        return true;
    }

    public boolean login(User user, HttpSession session) throws SQLException {
        User found = userRepository.findUserByName(user.getName());

        if(found == null || !Objects.equals(user.getPassword(), found.getPassword()))
            return false;

        session.setAttribute("name", user.getName());
        session.setAttribute("password", user.getPassword());
        sessions.putIfAbsent(session.getId(), user);

        return true;
    }

    public boolean isLoggedIn(HttpSession session) throws SQLException {
        String name = (String) session.getAttribute("name");

        if(name == null)
            return false;

        if(sessions.containsKey(session.getId())){
            return true;
        }

        User user = userRepository.findUserByName(name);

        if(user == null) {
            session.removeAttribute("name");
            session.removeAttribute("password");

            return false;
        }

        return Objects.equals(user.getPassword(), session.getAttribute("password"));
    }
}
