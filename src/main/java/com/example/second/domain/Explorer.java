package com.example.second.domain;


import java.io.File;
import java.nio.file.NotDirectoryException;

public class Explorer {
    public File[] getFiles(String path) throws NotDirectoryException {
        File dir = new File(path);

        if(!dir.isDirectory())
            throw new NotDirectoryException(path + " is not a directory");

        return dir.listFiles();
    }

    public static String filterPath(String path){
        return path
                .replace("%20", " ")
                .replace("\\", "/");
    }
}
