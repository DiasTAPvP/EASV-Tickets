package com.example.easvtickets.GUI.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminScreenController {

    @FXML
    private Button manageEntityButton;

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    /**Implement:
    * Table Refreshes
    * Initialize(?)
    * Error displays
    * Selection/Listener capabilities
    **/

    @FXML
    private void onManageEntityButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin-panel.fxml"));
        Parent root = loader.load();

        AdminPanelController adminPanelController = loader.getController();
        adminPanelController.setAdminScreenController(this);

        Stage stage = new Stage();
        stage.setTitle("Manage Entities");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
