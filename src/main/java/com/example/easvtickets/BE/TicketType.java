package com.example.easvtickets.BE;

public class TicketType {

    private int ticketTypeID; // Primary key
    private String typeName;
    private String description;

    // Constructor
    public TicketType(int ticketTypeID, String typeName, String description) {
        this.ticketTypeID = ticketTypeID;
        this.typeName = typeName;
        this.description = description;
    }

    // Getters
    public int getTicketTypeID() {
        return ticketTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setTicketTypeID(int ticketTypeID) {
        this.ticketTypeID = ticketTypeID;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}