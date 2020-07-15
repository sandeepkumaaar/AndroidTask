package com.example.androidtask;

public class UserHelperClass {

    String userName, userPassword, type;

    public UserHelperClass(){
    }

    public UserHelperClass(String userName, String userPassword, String type) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
