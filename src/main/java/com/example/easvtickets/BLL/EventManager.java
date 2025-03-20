package com.example.easvtickets.BLL;

import com.example.easvtickets.DAL.IEventDataAccess;
import com.example.easvtickets.DAL.DAO.EventDAO;
import com.example.easvtickets.BE.Events;

import java.io.IOException;
import java.util.List;

public class EventManager {

    private IEventDataAccess eventDAO;

    public EventManager() throws IOException {
        eventDAO = new EventDAO();
    }

    public List<Events> getAllEvents() throws Exception {
        return eventDAO.getAllEvents();
    }

    public Events createEvent(Events newEvent) throws Exception {
        return eventDAO.createEvent(newEvent);
    }

    public void deleteEvent(Events selectedEvent) throws Exception {
        eventDAO.deleteEvent(selectedEvent);
    }

    public void updateEvent(Events event) throws Exception {
        eventDAO.updateEvent(event);
    }
}