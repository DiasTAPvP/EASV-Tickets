package com.example.easvtickets.GUI.Controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CoordScreenController {

    private LoginController loginController;


    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}

@FXML
private void onTicketsPressed() throws IOException {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ticket-generator.fxml"));
    Parent root = loader.load();

    //Get the controller and set the controller
    TicketController ticketcontroller = loader.getController();
    ticketcontroller.setCoordScreenController(this);

    Stage coordStage = (Stage) ticketsButton.getScene().getWindow();
    coordStage.close();

    Stage stage = new Stage();
    stage.setTitle("Ticket Generator");
    stage.setScene(new Scene(root));
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();
}
