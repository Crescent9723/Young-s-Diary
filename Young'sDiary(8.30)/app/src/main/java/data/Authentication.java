package data;

/**
 * Created by Justin on 8/16/2015.
 */
public class Authentication {
    private String userID = "yychoi";
    private static Authentication ourInstance = new Authentication();

    public static Authentication getInstance() {
        return ourInstance;
    }

    private Authentication() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
