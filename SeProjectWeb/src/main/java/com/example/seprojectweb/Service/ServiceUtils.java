package com.example.seprojectweb.Service;

public class ServiceUtils {

    public static boolean isValidUsername(String username) {
        //TODO add conditions for valid username
        return username != null && username.length() != 0;
    }

    public static boolean isValidPassword(String password) {
        //TODO add conditions for valid password
        return password != null && password.length() != 0;
    }


}
