package com.example.seprojectweb.Domain.Security;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityRepresentative {
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean validatePass(String plainPassword, String hashedPassword){
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
