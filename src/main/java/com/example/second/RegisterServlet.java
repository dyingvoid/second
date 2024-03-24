package com.example.second;

import com.example.second.data.User;
import com.example.second.domain.services.AuthService;
import com.example.second.domain.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();
    private final AuthService authService = AuthService.getInstance();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (authService.isLoggedIn(request.getSession())) {
                response.sendRedirect("files");
                return;
            }
        } catch (SQLException ex) {
            response.sendRedirect("error");
        }

        request.getRequestDispatcher(GlobalManager.getPage("register.jsp"))
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = userService.createUser(request);
            boolean result = authService.register(user);

            if (result) {
                response.sendRedirect("login");
                return;
            }

            reloadPageWithError("User already exists.", request, response);
        } catch (IllegalArgumentException ex) {
            reloadPageWithError("Username or password does not match rules", request, response);
        } catch (SQLException exception) {
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher(GlobalManager.getPage("error.jsp"))
                    .forward(request, response);
        }
    }

    private void reloadPageWithError(String error, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher(GlobalManager.getPage("register.jsp"))
                .forward(request, response);
    }
}
