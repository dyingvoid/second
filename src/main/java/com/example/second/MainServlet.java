package com.example.second;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.NotDirectoryException;

@WebServlet("/files")
public class MainServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String main = request.getQueryString();
        if(main == null || main.isBlank()) {
            response.sendRedirect(request.getRequestURI() + "files?" + "C:/");
            return;
        }

        String path = URLDecoder.decode(main, StandardCharsets.UTF_8);
        File file = new File(path);

        if(file.isDirectory())
            ListFiles(file, request, response);
        else if (file.isFile())
            DownloadFile(file, request, response);
        else
            throw new InvalidObjectException("Nor a dir or a file.");
    }

    private void DownloadFile(File file, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if(!file.exists())
            throw new FileNotFoundException(file.getName() + "does not exist.");

        response.setContentType(getServletContext().getMimeType(file.getAbsolutePath()));
        response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName() + "\"");

        try(InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream()){
            byte[] buffer = new byte[4096];
            int bytesRead;

            while((bytesRead = in.read(buffer)) > 0){
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void ListFiles(File directory, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("path", directory);
        request.setAttribute("files", getFiles(directory.getAbsolutePath()));
        request.setAttribute("back", getPathBack(request));
        request.getRequestDispatcher("/WEB-INF/views/explorer.jsp")
                .forward(request, response);
    }

    public File[] getFiles(String path) throws NotDirectoryException {
        File dir = new File(path);

        if(!dir.isDirectory())
            throw new NotDirectoryException(path + " is not a directory");

        return dir.listFiles();
    }

    public String getPathBack(HttpServletRequest request){
        String defaultUri = request.getRequestURI() + "?";
        String defaultLocation = "C:/";
        String path = request.getQueryString();

        if(path == null || path.isBlank())
            return defaultUri + defaultLocation;

        int lastSlashIndex = path.lastIndexOf("/");

        if(lastSlashIndex == -1 || lastSlashIndex == path.indexOf("/"))
            return defaultUri + defaultLocation;

        return request.getRequestURI() + "?" + path.substring(0, lastSlashIndex);
    }
}
