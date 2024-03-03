package com.example.second;

public class OsChecker {
    public static String GetDefaultPath(){
        String os = System.getProperty("os.name");

        if(os.contains("Windows"))
            return "files?C:/";
        else
            return "files?/";
    }

    public static String GetDefaultLocation(){
        String os = System.getProperty("os.name");

        if(os.contains("Windows"))
            return "C:/";
        else
            return "/";
    }
}
