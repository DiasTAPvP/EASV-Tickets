package com.example.easvtickets.BE;

import java.sql.Timestamp;

public class Users {

    //Instance fields
    private int loginid;
    private String username;
    private String passwordhash;
    private boolean isadmin;
    private Timestamp createdat;

    //Constructor
    public Users(int loginid, String username, String passwordhash, boolean isadmin, Timestamp createdat) {
        this.loginid = loginid;
        this.username = username;
        this.passwordhash = passwordhash;
        this.isadmin = isadmin;
        this.createdat = createdat;
    }

    //Getters
    public int getLoginid() {
        return this.loginid;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPasswordhash() {
        return this.passwordhash;
    }
    public boolean getIsadmin() {
        return this.isadmin;
    }
    public Timestamp getCreatedat() {
        return this.createdat;
    }


    //Setters
    public void setLoginid(int value) {
        this.loginid = value;
    }
    public void setUsername(String value) {
        this.username = value;
    }
    public void setPasswordhash(String value) {
        this.passwordhash = value;
    }
    public void setIsadmin(boolean value) {
        this.isadmin = value;
    }
    public void setCreatedat(Timestamp value) {
        this.createdat = value;
    }



}
