package com.example.easvtickets.DAL.DAO;

import com.example.easvtickets.BE.Users;
import com.example.easvtickets.DAL.DBConnector;
import com.example.easvtickets.DAL.IUserDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDataAccess {

    private DBConnector dbConnector;

    public UserDAO() throws IOException {
        dbConnector = new DBConnector();
    }

    @Override
    public List<Users> getAllUsers() throws Exception {
        ArrayList<Users> allUsers = new ArrayList<>();

        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Logins";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                int loginid = rs.getInt("loginid");
                String username = rs.getString("username");
                String passwordhash = rs.getString("passwordhash");
                boolean isadmin = rs.getBoolean("isadmin");
                Timestamp createdat = rs.getTimestamp("createdat");

                Users user = new Users(loginid, username, passwordhash, isadmin, createdat);
                allUsers.add(user);
            }
            return allUsers;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get users from database.", ex);
        }
    }

    @Override
    public Users createUser(Users newUser) throws Exception {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "INSERT INTO Logins (username, passwordhash, isadmin, createdat) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newUser.getUsername());
            statement.setString(2, newUser.getPasswordhash());
            statement.setBoolean(3, newUser.getIsadmin());
            statement.setTimestamp(4, newUser.getCreatedat());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                newUser.setLoginid(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not create user in database.", e);
        }
        return newUser;
    }

    @Override
    public void deleteUser(Users users) throws Exception {
        String sql = "DELETE FROM Logins WHERE loginid = ?";

        try (Connection connection = dbConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, users.getLoginid());

            //Run the SQL statement
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not get user from database.", ex);
        }
    }

    public List<Users> getAssignedCoordinators(int eventId) throws Exception {
        List<Users> assignedCoordinators = new ArrayList<>();
        String sql = "SELECT u.* FROM Logins u " +
                "JOIN EventCoordinators ec ON u.loginid = ec.loginid " +
                "WHERE ec.eventid = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users(
                            rs.getInt("loginid"),
                            rs.getString("username"),
                            rs.getString("passwordhash"),
                            rs.getBoolean("isadmin"),
                            rs.getTimestamp("createdat")
                    );

                    assignedCoordinators.add(user);
                }
            }
        }

        return assignedCoordinators;
    }

    /**
     * Assigns a user as a coordinator to an event
     * @param userId The user's ID
     * @param eventId The event's ID
     * @throws Exception If there's an error during the database operation
     */
    public void assignCoordinatorToEvent(int userId, int eventId) throws Exception {
        // First check if this assignment already exists
        String checkSql = "SELECT COUNT(*) FROM EventCoordinators WHERE loginid = ? AND eventid = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, eventId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Assignment already exists, no need to insert
                    return;
                }
            }

            // Assignment doesn't exist, insert it
            String insertSql = "INSERT INTO EventCoordinators (loginid, eventid) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, eventId);
                insertStmt.executeUpdate();
            }
        }
    }

    public void removeCoordinatorFromEvent(int userId, int eventId) throws Exception {
        String sql = "DELETE FROM EventCoordinators WHERE loginid = ? AND eventid = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Could not remove coordinator from event.", e);
        }
    }

    @Override
    public void updateUser(Users user) throws Exception {

    }

    @Override
    public Users getUsername(String username) {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Logins WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Users(
                        resultSet.getInt("loginid"),
                        resultSet.getString("username"),
                        resultSet.getString("passwordhash"),
                        resultSet.getBoolean("isadmin"),
                        resultSet.getTimestamp("createdat")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}