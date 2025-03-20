package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.DAL.DAO.EventDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;

public class NewEventController {

    private CoordScreenController setCoordScreenController;
    private EventDAO eventDAO;

    public NewEventController() throws IOException {
        this.eventDAO = new EventDAO();
    }

    @FXML private TextField newTicketType1;
    @FXML private TextField newTicketType2;
    @FXML private TextField newTicketType3;
    @FXML private TextField newTicketType4;
    @FXML private TextField newFreeTicketType;
    @FXML private Button newEventSaveButton;
    @FXML private TextArea newEventInfo;
    @FXML private TextField newEventName;
    @FXML private TextField newEventLocation;
    @FXML private TextField newTotalTickets;
    @FXML private TextArea newEventNotes;
    @FXML private DatePicker newEventDate;

    /**
     * Implement:
     * Adding custom ticket types to the database and
     * reflecting those in the Ticket Generator for a given event
     * Functionality for adding a special ("free") ticket type to an event
     **/




    @FXML
    private void onCreate() {
        try {
            String eventName = newEventName.getText();
            String eventLocation = newEventLocation.getText();
            String eventDescription = newEventInfo.getText();
            Timestamp eventDate = Timestamp.valueOf(newEventDate.getValue().atStartOfDay());
            String eventNotes = newEventNotes.getText();
            int availableTickets = Integer.parseInt(newTotalTickets.getText());
            String optionalInformation = "";

            Events newEvent = new Events(0, eventName, eventDescription, eventDate, eventLocation, eventNotes, availableTickets, optionalInformation);
            eventDAO.createEvent(newEvent);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Event created successfully!");

            clearInputFields();

            Stage stage = (Stage) newEventSaveButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not create event: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void clearInputFields() {
        newEventName.clear();
        newEventLocation.clear();
        newEventInfo.clear();
        newEventNotes.clear();
        newTotalTickets.clear();
        newEventDate.setValue(null);
        newTicketType1.clear();
        newTicketType2.clear();
        newTicketType3.clear();
        newTicketType4.clear();
        newFreeTicketType.clear();
    }

    public void setCoordScreenController(CoordScreenController coordScreenController) {this.setCoordScreenController = coordScreenController;}
}