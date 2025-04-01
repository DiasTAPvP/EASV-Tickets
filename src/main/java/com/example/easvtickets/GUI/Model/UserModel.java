package com.example.easvtickets.GUI.Model;


import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BLL.UserManager;
import com.example.easvtickets.DAL.DAO.UserDAO;

public class UserModel {

    private final UserManager userManager;

    private final UserDAO userDAO = new UserDAO();


    public UserModel() throws Exception {
        userManager = new UserManager();


    }


    public void deleteUsers(Users selectedUser) throws Exception {
        //Delete from the database through the layers
        userManager.deleteUser(selectedUser);

        //Remove from the ObservableList
    }

}
