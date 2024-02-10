package com.example.second;

import com.example.second.domain.Explorer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/files")
public class MainServlet extends HttpServlet {
    private final Explorer explorer = new Explorer();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = Explorer.filterPath(request.getQueryString());
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
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());

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
        request.setAttribute("files", explorer.getFiles(directory.getAbsolutePath()));
        request.setAttribute("back", explorer.getPathBack(request));
        request.getRequestDispatcher("/WEB-INF/views/explorer.jsp")
                .forward(request, response);
    }
}
