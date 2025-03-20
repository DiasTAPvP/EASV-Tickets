package com.example.easvtickets.GUI.Controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class CreateUserController {

    private AdminPanelController adminPanelController;

    public void setAdminPanelController(AdminPanelController adminPanelController) {this.adminPanelController = adminPanelController;}


    /**Implement:
     * Error displays
     * onCreate functionality
     * Initialize (setting up a generated password immediately?)
     * Button functionality (save, cancel)
     * The ability to send an email to the created user/coordinator with their login information on creation
     * Checkbox functionality for the user type
     * Password generator and hider functionality
     */



    //Method to create a new user
    @FXML
    public void onCreateUser() throws Exception {

    }


    @FXML
    public void onCancelButtonPressed() {
        //Stage stage = (Stage) userCancelButton.getScene().getWindow();
        //stage.close();
    }


}
