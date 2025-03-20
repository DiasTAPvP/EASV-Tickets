package com.example.easvtickets.DAL;

import com.example.easvtickets.BE.Events;

import java.util.List;

public interface IEventDataAccess {

    List<Events> getAllEvents() throws Exception;

    Events createEvent(Events newEvent) throws Exception;

    void deleteEvent(Events event) throws Exception;

    void updateEvent(Events event) throws Exception;



}
