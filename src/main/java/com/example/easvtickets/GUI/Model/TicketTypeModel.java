package com.example.easvtickets.GUI.Model;

import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.BLL.TicketTypeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class TicketTypeModel {
    private TicketTypeManager ticketTypeManager;
    private ObservableList<TicketType> ticketTypes;

    public TicketTypeModel() throws Exception {
        ticketTypeManager = new TicketTypeManager();
        ticketTypes = FXCollections.observableArrayList();
        refreshTicketTypes();
    }

    public ObservableList<TicketType> getObservableList() {
        return ticketTypes;
    }

    public void refreshTicketTypes() throws Exception {
        List<TicketType> allTicketTypes = ticketTypeManager.getAllTicketTypes();
        ticketTypes.clear();
        ticketTypes.addAll(allTicketTypes);
    }

    public TicketType createTicketType(TicketType newTicketType) throws Exception {
        TicketType createdTicketType = ticketTypeManager.createTicketType(newTicketType);
        refreshTicketTypes();
        return createdTicketType;
    }

    public void deleteTicketType(TicketType ticketType) throws Exception {
        ticketTypeManager.deleteTicketType(ticketType);
        ticketTypes.remove(ticketType);
    }

    public void updateTicketType(TicketType ticketType) throws Exception {
        ticketTypeManager.updateTicketType(ticketType);
        refreshTicketTypes();
    }

    public List<TicketType> getTicketTypesForEvent(int eventId) throws Exception {
        return ticketTypeManager.getTicketTypesForEvent(eventId);
    }
}