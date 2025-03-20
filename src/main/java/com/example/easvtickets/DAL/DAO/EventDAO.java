package com.example.easvtickets.DAL.DAO;


import com.example.easvtickets.BE.Events;
import com.example.easvtickets.DAL.IEventDataAccess;

import java.util.List;

public class EventDAO implements IEventDataAccess {

    @Override
    public List<Events> getAllEvents() throws Exception {
        return List.of();
    }

    @Override
    public Events createEvent(Events newEvent) throws Exception {
        return null;
    }

    @Override
    public void deleteEvent(Events event) throws Exception {

    }

    @Override
    public void updateEvent(Events event) throws Exception {

    }
}
