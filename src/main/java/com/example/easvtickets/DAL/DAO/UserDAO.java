package com.example.easvtickets.DAL;

import com.example.easvtickets.BE.Users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO implements IUserDataAccess {

    private DBConnector dbConnector;

    public UserDAO() throws IOException {
        dbConnector = new DBConnector();
    }

    @Override
    public List<Users> getAllUsers() throws Exception {
        return null;
    }

    @Override
    public Users createUser(Users newUser) throws Exception {
        return null;
    }

    @Override
    public void deleteUser(Users user) throws Exception {

    }

    @Override
    public void updateUser(Users user) throws Exception {

    }

    @Override
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