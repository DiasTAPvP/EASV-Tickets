package com.example.easvtickets.GUI.Controller;


import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class CoordScreenController {

    @FXML
    private Button ticketsButton;
    @FXML
    private Button manageEventButton;
    @FXML
    private Button createEventButton;

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    /**Implement:
     * Table refreshes.
     * Initialize to set up tables on boot
     * Listener for the selected event to show event info
     * Distinction between your assigned events and all events
     */



    @FXML
    private void onTicketsPressed() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ticket-generator.fxml"));
        Parent root = loader.load();

        //Get the controller and set the controller
        TicketController ticketcontroller = loader.getController();
        ticketcontroller.setCoordScreenController(this);

        Stage stage = new Stage();
        stage.setTitle("Ticket Generator");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void onManageButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/coordinator-panel.fxml"));
        Parent root = loader.load();

        //Get the controller and set the controller
        CoordPanelController eventController = loader.getController();
        eventController.setCoordScreenController(this);

        Stage stage = new Stage();
        stage.setTitle("Manage Events");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void onCreateButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/create-event-view.fxml"));
        Parent root = loader.load();

        //Get the controller and set the controller
        NewEventController newEventController = loader.getController();
        newEventController.setCoordScreenController(this);

        Stage stage = new Stage();
        stage.setTitle("Create Event");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


}
