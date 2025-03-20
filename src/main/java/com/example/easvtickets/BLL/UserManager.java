package com.example.easvtickets.BLL;

import com.example.easvtickets.DAL.IUserDataAccess;
import com.example.easvtickets.DAL.DAO.UserDAO;
import com.example.easvtickets.BE.Users;

import java.io.IOException;
import java.util.List;

public class UserManager {

    private IUserDataAccess userDAO;

    public UserManager() throws IOException {
        userDAO = new UserDAO();
    }

    public List<Users> getAllUsers() throws Exception {
        return userDAO.getAllUsers();
    }

    public Users createUser(Users newUser) throws Exception {
        return userDAO.createUser(newUser);
    }

    public void deleteUser(Users user) throws Exception {
        userDAO.deleteUser(user);
    }

    public void updateUser(Users user) throws Exception {
        userDAO.updateUser(user);
    }

    public Users getUsername(String username) {
        return userDAO.getUsername(username);
    }
}