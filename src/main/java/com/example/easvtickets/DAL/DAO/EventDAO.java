package com.example.easvtickets.DAL.DAO;

import com.example.easvtickets.BE.Events;
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

    }

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
        }


    }
