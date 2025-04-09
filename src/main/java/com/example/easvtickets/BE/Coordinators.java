package com.example.easvtickets.BE;

public class Coordinators {

    private int eventID; // Foreign key to Events table
    private int loginID; // Foreign key to Logins table

    // Constructor
    public Coordinators(int eventID, int loginID) {
        this.eventID = eventID;
        this.loginID = loginID;
    }

    // Getters
    public int getEventID() {
        return eventID;
    }

    public int getLoginID() {
        return loginID;
    }

    // Setters
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setLoginID(int loginID) {
        this.loginID = loginID;
    }
}