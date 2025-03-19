package com.example.easvtickets.BE;

import java.sql.Timestamp;

public class Events {

    private int eventid;
    private String eventname;
    private String description;
    private Timestamp eventdate;
    private String location;
    private String notes;
    private int availabletickets;
    private String optionalinformation;


    //Constructor
    public Events(int eventid, String eventname, String description, Timestamp eventdate, String location, String notes, int availabletickets, String optionalinformation) {
        this.eventid = eventid;
        this.eventname = eventname;
        this.description = description;
        this.eventdate = eventdate;
        this.location = location;
        this.notes = notes;
        this.availabletickets = availabletickets;
        this.optionalinformation = optionalinformation;

    }

    //Getters
    public int getEventId() {
        return this.eventid;
    }
    public String getEventName() {
        return this.eventname;
    }
    public String getDescription() {
        return this.description;
    }
    public Timestamp getEventDate() {
        return this.eventdate;
    }

    public String getLocation() {
        return this.location;
    }
    public String getNotes() {
        return this.notes;
    }
    public int getAvailableTickets() {
        return this.availabletickets;
    }
    public String getOptionalInformation() {
        return this.optionalinformation;
    }

    //Setters
    public void setEventId(int value) {
        this.eventid = value;
    }
    public void setEventName(String value) {
        this.eventname = value;
    }
    public void setDescription(String value) {
        this.description = value;
    }
    public void setEventDate(Timestamp value) {
        this.eventdate = value;
    }
    public void setLocation(String value) {
        this.location = value;
    }
    public void setNotes(String value) {
        this.notes = value;
    }
    public void setAvailableTickets(int value) {
        this.availabletickets = value;
    }
    public void setOptionalInformation(String value) {
        this.optionalinformation = value;
    }




}
