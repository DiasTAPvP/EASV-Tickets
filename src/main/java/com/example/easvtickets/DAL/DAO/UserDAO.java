package com.example.easvtickets.DAL;

import com.example.easvtickets.BE.Users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private DBConnector dbConnector;

    public UserDAO() throws IOException {
        dbConnector = new DBConnector();
    }

    public Users getUsername(String username) {
        try (Connection connection = dbConnector.getConnection()) {
            String sql = "SELECT * FROM Users WHERE username = ?";
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