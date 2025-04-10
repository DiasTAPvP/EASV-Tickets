package com.example.easvtickets.DAL;

import com.example.easvtickets.BE.TicketType;

import java.util.List;

public interface ITicketDataAccess {


    List<TicketType> getAllTicketTypes() throws Exception;

    TicketType createTicketType(TicketType newTicketType) throws Exception;

    void deleteTicketType(TicketType ticketType) throws Exception;

    void updateTicketType(TicketType ticketType) throws Exception;

    List<TicketType> getTicketTypesForEvent(int eventId);
}
