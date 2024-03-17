package com.example.second;

import com.example.second.domain.services.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class deleteServlet extends HttpServlet {
    private final UserService userService = GlobalManager.userService;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        userService.deleteSession(session.getId());
        session.invalidate();
        response.sendRedirect("login");
    }
}
