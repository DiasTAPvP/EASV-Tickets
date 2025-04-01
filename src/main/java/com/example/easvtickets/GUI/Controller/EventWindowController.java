package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.DAL.DAO.EventDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Timestamp;

public class EventWindowController {

    private CoordScreenController setCoordScreenController;
    private EventDAO eventDAO;

    public EventWindowController() throws IOException {
        this.eventDAO = new EventDAO();
    }
    @FXML private Button newEventSaveButton;
    @FXML private TextArea newEventInfo;
    @FXML private TextField newEventName;
    @FXML private TextField newEventLocation;
    @FXML private TextField newTotalTickets;
    @FXML private TextArea newEventNotes;
    @FXML private DatePicker newEventDate;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;

    /**
     * Implement:
     * Adding custom ticket types to the database and
     * reflecting those in the Ticket Generator for a given event
     * Functionality for adding a special ("free") ticket type to an event
     **/




    @FXML
    public void initialize() {
        // Set up hour spinner (0-23)
        SpinnerValueFactory<Integer> hourFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        hourSpinner.setValueFactory(hourFactory);

        // Set up minute spinner (0-59)
        SpinnerValueFactory<Integer> minuteFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteFactory);

        // Format spinner to always show two digits
        hourSpinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        });

        minuteSpinner.getValueFactory().setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        });
    }


    @FXML
    private void onCreate() {
        try {
            String eventName = newEventName.getText();
            String eventLocation = newEventLocation.getText();
            String eventDescription = newEventInfo.getText();

            // Get date from DatePicker
            java.time.LocalDate localDate = newEventDate.getValue();

            // Get time from spinners
            int hours = hourSpinner.getValue();
            int minutes = minuteSpinner.getValue();

            // Combine date and time
            java.time.LocalDateTime dateTime = localDate.atTime(hours, minutes);
            Timestamp eventDate = Timestamp.valueOf(dateTime);

            String eventNotes = newEventNotes.getText();
            int availableTickets = Integer.parseInt(newTotalTickets.getText());
            String optionalInformation = "";

            Events newEvent = new Events(0, eventName, eventDescription, eventDate, eventLocation,
                    eventNotes, availableTickets, optionalInformation);
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
        hourSpinner.getValueFactory().setValue(12);
        minuteSpinner.getValueFactory().setValue(0);
    }

    public void setCoordScreenController(CoordScreenController coordScreenController) {this.setCoordScreenController = coordScreenController;}

    private Events selectedEvent;

    public void setSelectedEvent(Events event) {
        System.out.println("Editing event: " + event);
        this.selectedEvent = event;
        prefillFields();
    }

    private void prefillFields() {
        if (selectedEvent != null) {
            newEventName.setText(selectedEvent.getEventName());
            newEventLocation.setText(selectedEvent.getLocation());
            newEventInfo.setText(selectedEvent.getDescription());

            // Set date in DatePicker
            newEventDate.setValue(selectedEvent.getEventDate().toLocalDateTime().toLocalDate());

            // Set time in spinners
            hourSpinner.getValueFactory().setValue(selectedEvent.getEventDate().toLocalDateTime().getHour());
            minuteSpinner.getValueFactory().setValue(selectedEvent.getEventDate().toLocalDateTime().getMinute());

            newEventNotes.setText(selectedEvent.getNotes());
            newTotalTickets.setText(String.valueOf(selectedEvent.getAvailableTickets()));
        }
    }
    }
