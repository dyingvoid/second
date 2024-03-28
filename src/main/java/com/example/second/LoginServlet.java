package com.example.second;

import com.example.second.data.User;
import com.example.second.domain.services.AuthService;
import com.example.second.domain.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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

            request.getRequestDispatcher(GlobalManager.getPage("login.jsp"))
                    .forward(request, response);
        } catch (SQLException sqlException) {
            throw new IllegalStateException(
                    "Database error:" + sqlException.getMessage()
            );
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = userService.createUser(request);

            if(!authService.login(user, request.getSession())){
                request.setAttribute("error", "Wrong username or password");
                request.getRequestDispatcher(GlobalManager.getPage("login.jsp"))
                        .forward(request, response);

                return;
            }

            response.sendRedirect("files");
        } catch (SQLException sqlException) {
            throw new IllegalStateException(
                    "Datanase error:" + sqlException.getMessage()
            );
        } catch (IllegalArgumentException exception){
            request.setAttribute("error", "User fields are empty.");
            request.getRequestDispatcher(GlobalManager.getPage("login.jsp")).forward(request, response);
        }
    }
}
