package Service;

public class ServiceUtils {

    public static boolean isValidUsername(String username){
        //TODO add conditions for valid username
        if(username == null || username.length() == 0) return false;
        return true;
    }

    public static boolean isValidPassword(String password){
        //TODO add conditions for valid password
        if(password == null || password.length() == 0) return false;
        return true;
    }



}
