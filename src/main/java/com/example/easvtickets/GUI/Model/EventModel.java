package com.example.easvtickets.GUI.Model;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BLL.EventManager;
import com.example.easvtickets.DAL.DAO.EventDAO;

public class EventModel {

    private final EventManager eventManager;

    private final EventDAO eventDAO = new EventDAO();


    public EventModel() throws Exception {
    eventManager = new EventManager();

}


    public void deleteEvents(Events selectedEvent) throws Exception {
        //Delete from the database through the layers
        eventManager.deleteEvent(selectedEvent);

        //Remove from the ObservableList
    }




}
