package com.example.easvtickets.GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPanelController {

    private AdminScreenController adminScreenController;

    public void setAdminScreenController(AdminScreenController adminScreenController) {this.adminScreenController = adminScreenController;}


    /**Implement:
     * Some sort of listener for what was selected to be managed
     * Delete button functionality. Different between events and users.
     * **/


    @FXML
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
    }

    @FXML
    private void onAdminDeleteButtonPressed() throws Exception {
        //Implement functionality

    }


}
