package com.example.easvtickets.BLL;

import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.DAL.DAO.TicketTypeDAO;
import com.example.easvtickets.DAL.ITicketDataAccess;

import java.io.IOException;
import java.util.List;

public class TicketTypeManager {
    private ITicketDataAccess ticketTypeDAO;

    public TicketTypeManager() throws IOException {
        ticketTypeDAO = new TicketTypeDAO();
    }


    public List<TicketType> getAllTicketTypes() throws Exception {
        return ticketTypeDAO.getAllTicketTypes();
    }

    public TicketType createTicketType(TicketType newTicketType) throws Exception {
        return ticketTypeDAO.createTicketType(newTicketType);
    }

    public void deleteTicketType(TicketType ticketType) throws Exception {
        ticketTypeDAO.deleteTicketType(ticketType);
    }

    public void updateTicketType(TicketType ticketType) throws Exception {
        ticketTypeDAO.updateTicketType(ticketType);
    }
}