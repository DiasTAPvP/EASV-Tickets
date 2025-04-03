package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BE.Events;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class AdminPanelController {

    private AdminScreenController adminScreenController;

    public void setAdminScreenController(AdminScreenController adminScreenController) {this.adminScreenController = adminScreenController;}


    /**Implement:
     * Some sort of listener for what was selected to be managed
     * Delete button functionality. Different between events and users.
     * **/


    /*@FXML
    private void onCreateUserButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/create-user.fxml"));
        Parent root = loader.load();

        CreateUserController createUserScreenController = loader.getController();
        createUserScreenController.setAdminPanelController(this);

        Stage stage = new Stage();
        stage.setTitle("Create New Users");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }*/

    @FXML
    private void onAdminDeleteButtonPressed() throws Exception {
        //Implement functionality
        /*int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the event or coordinator?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            Events selectedEvent = (Events) eventTableAdmin.getSelectionModel().getSelectedItem();
            Users selectedUser = (Users) userTableAdmin.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                eventDAO.deleteEvent(selectedEvent);
                eventTableAdmin.getItems().remove(selectedEvent);
                System.out.println("Event deleted succesfully.");

            } else if (selectedUser != null) {
                userDAO.deleteUser(selectedUser);
                userTableAdmin.getItems().remove(selectedUser);
                System.out.println("User deleted succesfully.");

            } else {
                System.out.println("No event or user selected.");

            }
        }
    }*/
    }


}
