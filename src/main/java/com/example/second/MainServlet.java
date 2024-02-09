package com.example.second;

import com.example.second.domain.Explorer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/files")
public class MainServlet extends HttpServlet {
    private final Explorer explorer = new Explorer();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = Explorer.filterPath(request.getQueryString());

        request.setAttribute("path", path);
        request.setAttribute("files", explorer.getFiles(path));
        request.getRequestDispatcher("/WEB-INF/views/explorer.jsp")
                .forward(request, response);
    }
}
