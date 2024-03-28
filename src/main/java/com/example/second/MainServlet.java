package com.example.second;

import com.example.second.domain.services.AuthService;
import com.example.second.domain.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@WebServlet("/files")
public class MainServlet extends HttpServlet {
    private final UserService userService = GlobalManager.userService;
    private final AuthService authService = AuthService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionId = request.getSession().getId();

        try {
            if (!authService.isLoggedIn(request.getSession())) {
                response.sendRedirect("login");
                return;
            }
        } catch (SQLException ex){
            response.sendRedirect("error");
        }

        String uri = request.getRequestURI();
        String main = request.getQueryString();
        String defaultPath = GlobalManager.getDefaultPath(request);
        String editedDefaultPath = defaultPath.substring(1);

        Path homeDir = Paths.get(URLDecoder.decode(editedDefaultPath, StandardCharsets.UTF_8));

        if(!Files.exists(homeDir))
            new File(String.valueOf(homeDir)).mkdirs();

        if (main == null || !main.startsWith(editedDefaultPath)) {
            response.sendRedirect(uri + defaultPath);
            return;
        }

        String path = URLDecoder.decode(main, StandardCharsets.UTF_8);
        File file = new File(path);

        if (file.isDirectory())
            listFiles(file, request, response);
        else if (file.isFile())
            downloadFile(file, request, response);
        else
            throw new InvalidObjectException("Nor a dir or a file.");
    }

    private void downloadFile(File file, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!file.exists())
            throw new FileNotFoundException(file.getName() + "does not exist.");

        response.setContentType(getServletContext().getMimeType(file.getAbsolutePath()));
        response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName() + "\"");

        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void listFiles(File directory, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("path", directory);
        request.setAttribute("files", getFiles(directory.getAbsolutePath()));
        request.setAttribute("back", getPathBack(request));
        request.getRequestDispatcher("/WEB-INF/views/explorer.jsp")
                .forward(request, response);
    }

    public File[] getFiles(String path) throws NotDirectoryException {
        File dir = new File(path);

        if (!dir.isDirectory())
            throw new NotDirectoryException(path + " is not a directory");

        return dir.listFiles();
    }

    public String getPathBack(HttpServletRequest request) {
        String defaultUri = request.getRequestURI();
        String defaultLocation = GlobalManager.getDefaultPath(request);
        String path = request.getQueryString();

        if (path == null || path.isBlank())
            return defaultUri + defaultLocation;

        int lastSlashIndex = path.lastIndexOf("/");

        if (lastSlashIndex == -1 || lastSlashIndex == defaultLocation.lastIndexOf("/") - 1)
            return defaultUri + defaultLocation;

        return request.getRequestURI() + "?" + path.substring(0, lastSlashIndex);
    }
}
