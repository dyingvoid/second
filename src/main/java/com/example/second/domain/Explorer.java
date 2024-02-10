package com.example.second.domain;


import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.NotDirectoryException;

public class Explorer {
    public File[] getFiles(String path) throws NotDirectoryException {
        File dir = new File(path);

        if(!dir.isDirectory())
            throw new NotDirectoryException(path + " is not a directory");

        return dir.listFiles();
    }

    public static String filterPath(String path){
        if(path == null)
            return null;

        return path
                .replace("%20", " ")
                .replace("\\", "/");
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
