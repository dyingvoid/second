package com.example.second;

import com.example.second.data.User;
import com.example.second.domain.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = GlobalManager.userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(userService.getUserBySessionId(request.getSession().getId()) != null)
            response.sendRedirect("files");

        showRegisterPage(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = userService.createUser(request);

        if (!userService.validateUser(user)) {
            response.sendRedirect("register");
            return;
        }

        userService.addUser(user);
        response.sendRedirect("login");
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(GlobalManager.getPage("register.jsp"))
                .forward(request, response);
    }
}
