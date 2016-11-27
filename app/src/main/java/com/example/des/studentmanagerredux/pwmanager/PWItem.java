package com.example.des.studentmanagerredux.pwmanager;

/**
 * Created by Nikhil on 11/26/2016.
 */

public class PWItem {
    private String entryTitle;
    private String userName;
    private String password;

    public PWItem(String entryTitle, String userName, String password) {
        this.entryTitle = entryTitle;
        this.userName = userName;
        this.password = password;
    }

    public String getTitle() { return entryTitle;}

    public String getUserName() { return userName;}

    public String getPassword() { return password;}

    public void setUserName(String newUserName) { this.userName = newUserName; }

    public void setPassword(String newPassword) { this.password = newPassword; }
}
