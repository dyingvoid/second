package com.example.second;

import com.example.second.data.User;
import com.example.second.domain.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = GlobalManager.userService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        if (userService.getUserBySessionId(sessionId) != null) {
            response.sendRedirect("files");
            return;
        }

        String name = (String) session.getAttribute("name");
        String password = (String) session.getAttribute("password");

        if (name == null) {
            showLoginPage(request, response);
        } else {
            User user = new User(name, password);

            if(userService.getUserByName(name) == null)
                userService.addUser(user);

            userService.addSession(sessionId, user);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User registeredUser = userService.getUserByName(name);

        if (Objects.equals(name, registeredUser.getName()) &&
                Objects.equals(password, registeredUser.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("name", name);
            session.setAttribute("password", password);

            userService.addSession(request.getSession().getId(), registeredUser);
            response.sendRedirect("files");

            return;
        }

        response.sendRedirect("login");
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(GlobalManager.getPage("register.jsp"))
                .forward(request, response);
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(GlobalManager.getPage("login.jsp"))
                .forward(request, response);
    }
}
