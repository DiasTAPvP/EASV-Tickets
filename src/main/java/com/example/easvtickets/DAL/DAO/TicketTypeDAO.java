package com.example.easvtickets.DAL.DAO;


import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.BE.Tickets;
import com.example.easvtickets.DAL.DBConnector;
import com.example.easvtickets.DAL.ITicketDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketTypeDAO implements ITicketDataAccess {

    private DBConnector dbConnector;

    public TicketTypeDAO() throws IOException {
        dbConnector = new DBConnector();
    }


    @Override
    public List<TicketType> getAllTicketTypes() throws Exception {
        List<TicketType> ticketTypeList = new ArrayList<>();
        String sql = "SELECT * FROM TicketTypes";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TicketType ticketType = new TicketType(
                        rs.getInt ("ticketTypeID"),
                        rs.getString("typeName"),
                        rs.getString("description")
                );
                ticketTypeList.add(ticketType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not retrieve ticket types", e);
        }
        return ticketTypeList;
    }

    @Override
    public TicketType createTicketType(TicketType newTicketType) throws Exception {
        try (Connection conn = dbConnector.getConnection()) {
            String sql = "INSERT INTO TicketTypes (typeName, description) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, newTicketType.getTypeName());
            ps.setString(2, newTicketType.getDescription());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                newTicketType.setTicketTypeID(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not create ticket type", e);
        }
        return newTicketType;
    }

    @Override
    public void deleteTicketType(TicketType ticketType) throws Exception {
        String sql = "DELETE FROM TicketTypes WHERE ticketTypeID = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketType.getTicketTypeID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not delete ticket type", e);
        }
    }

    @Override
    public void updateTicketType(TicketType ticketType) throws Exception {
        String sql = "UPDATE TicketTypes SET typeName = ?, description = ? WHERE ticketTypeID = ?";
        DBConnector dbConnector = new DBConnector();

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketType.getTypeName());
            ps.setString(2, ticketType.getDescription());
            ps.setInt(3, ticketType.getTicketTypeID());

            ps.executeUpdate();
            System.out.println("Ticket type updated successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not update ticket type", e);
        }
    }

}
