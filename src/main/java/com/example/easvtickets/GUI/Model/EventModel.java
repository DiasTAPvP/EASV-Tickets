package com.example.easvtickets.GUI.Model;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.Tickets;
import com.example.easvtickets.BLL.EventManager;
import com.example.easvtickets.DAL.DAO.EventDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventModel {

    private final EventManager eventManager;

    private final EventDAO eventDAO = new EventDAO();

    private final ObservableList<Events> eventsToBeViewed = FXCollections.observableArrayList();


    public EventModel() throws Exception {
    eventManager = new EventManager();
    }

    public void createEvent(Events newEvent) throws Exception {
        //Create the event through the layers
        Events eventCreated = eventManager.createEvent(newEvent);

        //Add to the ObservableList
        eventsToBeViewed.add(eventCreated);
        System.out.println("Event added to ObservableList" + eventCreated);
    }

    public ObservableList<Events> getObservableList() {
        return eventsToBeViewed;
    }

    public void addTicketToEvent(Tickets ticket) throws Exception {
        eventManager.addTicket(ticket);
    }

    public void deleteEvents(Events selectedEvent) throws Exception {
        // Delete from the database through the layers
        eventManager.deleteEvent(selectedEvent);

        // Remove from the ObservableList
        eventsToBeViewed.remove(selectedEvent);
    }

    public void refreshEvents() throws Exception {
        //Refresh the ObservableList
        eventsToBeViewed.clear();
        eventsToBeViewed.addAll(eventManager.getAllEvents());
    }


    public void updateEvent(Events selectedEvent) throws Exception {
        //Update the event through the layers
        eventManager.updateEvent(selectedEvent);

        //Update the ObservableList
        refreshEvents();

    }
}
