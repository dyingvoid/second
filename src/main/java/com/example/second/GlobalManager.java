package com.example.second;

import com.example.second.data.UserRepository;
import com.example.second.domain.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;

public class GlobalManager {
    public static UserService userService = UserService.getInstance();

    public static String getDefaultPath(HttpServletRequest request) {
        String os = System.getProperty("os.name");
        String path = "";

        if (os.contains("Windows"))
            path += "?C:/Users/Dying/Programming/JavaProject/";
        else
            path += "?/";

        String username = userService
                .getUserBySessionId(request.getSession().getId())
                .getName();

        if(username == null)
            throw new NullPointerException();

        path += username;

        return path;
    }

    public static String getPage(String name) {
        return "/WEB-INF/views/" + name;
    }
}
