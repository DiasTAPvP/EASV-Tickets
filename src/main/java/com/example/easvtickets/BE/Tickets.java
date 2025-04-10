package com.example.easvtickets.BE;

import java.sql.Timestamp;

public class Tickets {

    private int ticketID;
    private int ticketTypeID;
    private int eventID;
    private Timestamp purchaseDate;



    // Constructor
    public Tickets(int ticketID, int ticketTypeID, int eventID) {
        this.ticketID = ticketID;
        this.ticketTypeID = ticketTypeID;
        this.purchaseDate = new Timestamp(System.currentTimeMillis());
        this.eventID = eventID;
    }

    // Getters 123
    public int getTicketID() {
        return ticketID;
    }

    public int getTicketTypeID() {
        return ticketTypeID;
    }

    public int getEventID() {
        return eventID;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    // Setters
    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public void setTicketTypeID(int ticketTypeID) {
        this.ticketTypeID = ticketTypeID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}