package com.example.easvtickets.DAL.DAO;

import com.example.easvtickets.BE.Users;
import com.example.easvtickets.DAL.DBConnector;
import com.example.easvtickets.DAL.IUserDataAccess;

import java.io.IOException;
import java.sql.*;
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
        }
        return newUser;
    }

    @Override
    public void deleteUser(Users users) throws Exception {
        String sql = "DELETE FROM dbo.Users WHERE loginid = ?";

        try (Connection connection = dbConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, users.getLoginid());

            //Run the SQL statement
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not get user from database.", ex);
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