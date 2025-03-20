package com.example.easvtickets.GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class NewEventController {

    
    @FXML private TextField ticketType1;
    @FXML private TextField ticketType2;
    @FXML private TextField ticketType3;
    @FXML private TextField ticketType4;
    @FXML private TextField freeTicketType;
    @FXML private Button eventSaveButton;
    @FXML private Checkbox freeTicketCheckBox;
    @FXML private TextArea newEventInfo;


    /**Implement:
     * Functionality for creating a new event and adding it to the database
     * Error displays
     * The ability to add event info when you create the event
     * Save button (= create event)
     * Adding custom ticket types to the database and
     * reflecting those in the Ticket Generator for a given event
     * Functionality for adding a special ("free") ticket type to an event
     * **/


    private CoordScreenController setCoordScreenController;


    public void setCoordScreenController(CoordScreenController coordScreenController) {this.setCoordScreenController = coordScreenController;}

    @FXML
    private void initialize() {
        ticketType1.setText("");
        ticketType2.setText("");
        ticketType3.setText("");
        ticketType4.setText("");
        freeTicketType.setText("");

        newEventInfo.setText("");


        freeTicketCheckBox.setEnabled(false);
    }

    /*public void onCreate() throws Exception {
        String ticketType1 = ticketType1.getText();
        String ticketType2 = ticketType2.getText();
        String ticketType3 = ticketType3.getText();
        String ticketType4 = ticketType4.getText();
        String freeTicketType = freeTicketType.getText();
        String newEventInfo = newEventInfo.getText();

        //Create new event
        //Add event to database
        //Close the window
        Stage stage = (Stage) eventSaveButton.getScene().getWindow();
        stage.close();



    }*/







    }



