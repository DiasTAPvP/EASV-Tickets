package com.example.easvtickets.DAL.DAO;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.Tickets;
import com.example.easvtickets.DAL.DBConnector;
import com.example.easvtickets.DAL.IEventDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements IEventDataAccess {

    private DBConnector dbConnector;

    public EventDAO() throws IOException {
        dbConnector = new DBConnector();
    }

    @Override
    public List<Events> getAllEvents() throws Exception {
        List<Events> eventsList = new ArrayList<>();
        String sql = "SELECT * FROM Events";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Events event = new Events(
                        rs.getInt("eventID"),
                        rs.getString("eventname"),
                        rs.getString("description"),
                        rs.getTimestamp("eventdate"),
                        rs.getString("location"),
                        rs.getString("notes"),
                        rs.getInt("availabletickets"),
                        rs.getString("optionalinformation")
                );
                eventsList.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not retrieve events", e);
        }
        return eventsList;
    }

    @Override
    public List<Tickets> getAllTickets() throws Exception {
        List<Tickets> ticketsList = new ArrayList<>();
        String sql = "SELECT * FROM Tickets";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tickets ticket = new Tickets(
                        rs.getInt("ticketID"),
                        rs.getInt("ticketTypeID"),
                        rs.getInt("eventID")
                );
                ticketsList.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not retrieve tickets", e);
        }
        return ticketsList;
    }

    @Override
    public Events createEvent(Events newEvent) throws Exception {
        String eventSql = "INSERT INTO Events (eventname, description, eventdate, location, notes, availabletickets, optionalinformation) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = dbConnector.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // Insert the event
            PreparedStatement eventStmt = conn.prepareStatement(eventSql, Statement.RETURN_GENERATED_KEYS);
            eventStmt.setString(1, newEvent.getEventName());
            eventStmt.setString(2, newEvent.getDescription());
            eventStmt.setTimestamp(3, newEvent.getEventDate());
            eventStmt.setString(4, newEvent.getLocation());
            eventStmt.setString(5, newEvent.getNotes());
            eventStmt.setInt(6, newEvent.getAvailableTickets());
            eventStmt.setString(7, newEvent.getOptionalInformation());

            int affectedRows = eventStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            // Get the generated ID
            ResultSet generatedKeys = eventStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                newEvent.setEventId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating event failed, no ID obtained.");
            }

            // Commit transaction
            conn.commit();
            return newEvent;
        } catch (SQLException e) {
            // Rollback transaction on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new Exception("Could not roll back transaction", ex);
                }
            }
            throw new Exception("Could not create event", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Log this, but don't override the original exception
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void createTicket(Tickets ticket) throws Exception {
        String sql = "INSERT INTO Tickets (ticketTypeID, eventID) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, ticket.getTicketTypeID());
            stmt.setInt(2, ticket.getEventID());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating ticket failed, no rows affected.");
            }

            // Get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setTicketID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating ticket failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Could not create ticket", e);
        }
    }

    /* Old createEvent method without transaction handling
    @Override
    public Events createEvent(Events newEvent) throws Exception {
        String sql = "INSERT INTO Events (eventname, description, eventdate, location, notes, availabletickets, optionalinformation) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, newEvent.getEventName());
            ps.setString(2, newEvent.getDescription());
            ps.setTimestamp(3, newEvent.getEventDate());
            ps.setString(4, newEvent.getLocation());
            ps.setString(5, newEvent.getNotes());
            ps.setInt(6, newEvent.getAvailableTickets());
            ps.setString(7, newEvent.getOptionalInformation());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newEvent.setEventId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating event failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not create event", e);
        }
        return newEvent;
    }
     */

    @Override
    public void deleteEvent(Events event) throws Exception {
        String deleteTicketsSql = "DELETE FROM Tickets WHERE eventID = ?";
        String deleteEventSql = "DELETE FROM Events WHERE eventID = ?";

        try (Connection conn = dbConnector.getConnection()) {
            // Disable auto-commit to start a transaction
            conn.setAutoCommit(false);

            // Delete tickets linked to the event
            try (PreparedStatement deleteTicketsStmt = conn.prepareStatement(deleteTicketsSql)) {
                deleteTicketsStmt.setInt(1, event.getEventId());
                deleteTicketsStmt.executeUpdate();
            }

            // Delete the event
            try (PreparedStatement deleteEventStmt = conn.prepareStatement(deleteEventSql)) {
                deleteEventStmt.setInt(1, event.getEventId());
                deleteEventStmt.executeUpdate();
            }

            // Commit the transaction
            conn.commit();
        } catch (Exception e) {
            // Roll back the transaction in case of an error
            try (Connection conn = dbConnector.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new Exception("Transaction failed: " + e.getMessage(), e);
        }
    }

    /* Old deleteEvent method
    @Override
    public void deleteEvent(Events event) throws Exception {
        String sql = "DELETE FROM Events WHERE eventid = ?";

        try (Connection connection = dbConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, event.getEventId());

            //Run the SQL statement
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not get user from database.", ex);
        }

    } */

    @Override
    public void updateEvent(Events event) throws Exception {
        String updateEventSql = "UPDATE Events SET eventname = ?, description = ?, eventdate = ?, location = ?, notes = ?, availabletickets = ?, optionalinformation = ? WHERE eventID = ?";

        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement updateEventStmt = conn.prepareStatement(updateEventSql);
            updateEventStmt.setString(1, event.getEventName());
            updateEventStmt.setString(2, event.getDescription());
            updateEventStmt.setTimestamp(3, event.getEventDate());
            updateEventStmt.setString(4, event.getLocation());
            updateEventStmt.setString(5, event.getNotes());
            updateEventStmt.setInt(6, event.getAvailableTickets());
            updateEventStmt.setString(7, event.getOptionalInformation());
            updateEventStmt.setInt(8, event.getEventId());

            updateEventStmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Could not update event", e);
        }
    }


    /* Old updateEvent method
    @Override
    public void updateEvent(Events event) throws Exception {
        String sql = "UPDATE Events SET eventname = ?, description = ?, eventdate = ?, location = ?, notes = ?, availabletickets = ?, optionalinformation = ? WHERE eventid = ?";
        DBConnector dbConnector = new DBConnector();

        try (Connection conn = dbConnector.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, event.getEventName());
                stmt.setString(2, event.getDescription());
                stmt.setTimestamp(3, event.getEventDate());
                stmt.setString(4, event.getLocation());
                stmt.setString(5, event.getNotes());
                stmt.setInt(6, event.getAvailableTickets());
                stmt.setString(7, event.getOptionalInformation());
                stmt.setInt(8, event.getEventId());

                //Run the SQL statement
                stmt.executeUpdate();
                System.out.println("Event updated successfully.");

            } catch (SQLException ex) {
                throw new Exception("Could not update the event in the database.", ex);
            }
        }*/


    }
