package com.example.copd.Model;

/**
 * Created by Jinsi on 2018/3/16.
 */

public class User {
    public static String me;
    public static String bindID = null;
    private String userName;
    private String userID;
    public static String password;

    public User(String userNname, String userID) {
        this.userName = userNname;
        this.userID = userID;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserNname(String userNname) {
        this.userName = userNname;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
