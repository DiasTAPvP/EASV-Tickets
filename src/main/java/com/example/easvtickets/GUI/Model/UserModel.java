package com.example.easvtickets.GUI.Model;


import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BLL.UserManager;
import com.example.easvtickets.DAL.DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {

    private final UserManager userManager;

    private final UserDAO userDAO = new UserDAO();

    private final ObservableList<Users> usersToBeViewed = FXCollections.observableArrayList();


    public UserModel() throws Exception {
        userManager = new UserManager();


    }

    public ObservableList<Users> getObservableList() {
        return usersToBeViewed;
    }


    public void deleteUsers(Users selectedUser) throws Exception {
        //Delete from the database through the layers
        userManager.deleteUser(selectedUser);

        //Remove from the ObservableList
        usersToBeViewed.remove(selectedUser);
    }

    public void refreshUsers() throws Exception {
        //Refresh the ObservableList
        usersToBeViewed.clear();
        usersToBeViewed.addAll(userManager.getAllUsers());
    }

}
