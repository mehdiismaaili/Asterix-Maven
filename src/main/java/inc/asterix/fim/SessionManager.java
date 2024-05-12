/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inc.asterix.fim;

/**
 *
 * @author mehdi
 */
public class SessionManager {
    
    // declaring the variable that will host the retreived user_name or email
    private static String currentUserEmail;
    
    // this methode will hold the store user email when he logs in 
    public static void loginUser(String userEmail) {
        currentUserEmail = userEmail;
    }

    // this methode set the user email the  SessionManager class to null meaning user will be loged out
    public static void logoutUser() {
        currentUserEmail = null;
    }
    
    // this methode will retrieve the user email and for the other parts of application to use it
    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }
    
    // this methode will check if the user email is stored in the LoginUser methode by doing a test
    public static boolean isUserLoggedIn() {
        return currentUserEmail != null;
    }
    
}
