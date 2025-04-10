package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.BE.Tickets;
import com.example.easvtickets.DAL.DAO.EventDAO;
import com.example.easvtickets.DAL.DAO.TicketTypeDAO;
import com.example.easvtickets.GUI.Model.EventModel;
import com.example.easvtickets.GUI.Model.TicketTypeModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class EventWindowController {

    private CoordScreenController coordScreenController;
    private EventDAO eventDAO;
    private EventModel eventModel;
    private TicketTypeModel ticketTypeModel;
    private TicketTypeDAO ticketTypeDAO;

    public EventWindowController() throws Exception {
        this.eventDAO = new EventDAO();
        this.eventModel = new EventModel();
        this.ticketTypeModel = new TicketTypeModel();
        this.ticketTypeDAO = new TicketTypeDAO();
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
    @FXML private ComboBox<String> ticketTypeBox;


    @FXML
    private void initialize() {
        // Set up hour spinner (0-23)
        SpinnerValueFactory<Integer> hourFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        hourSpinner.setValueFactory(hourFactory);

        // Set up minute spinner (0-59)
        SpinnerValueFactory<Integer> minuteFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteFactory);

        // Format spinners to always show two digits
        formatSpinner(hourSpinner);
        formatSpinner(minuteSpinner);

        // Initialize the ticket type selection UI
        initTicketTypeSelection();
    }

    private void formatSpinner(Spinner<Integer> spinner) {
        spinner.getValueFactory().setConverter(new StringConverter<Integer>() {
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

    private List<TicketType> selectedTicketTypes = new ArrayList<>();

    private void initTicketTypeSelection() {
        // Clear any existing items
        ticketTypeBox.getItems().clear();

        // Set a prompt text
        ticketTypeBox.setPromptText("Select Ticket Types");

        try {
            // Load ticket types from the database
            List<TicketType> ticketTypes = ticketTypeModel.getObservableList();

            if (ticketTypes.isEmpty()) {
                // Handle the case where there are no ticket types
                ticketTypeBox.getItems().add("No ticket types available");
                ticketTypeBox.setDisable(true);
            } else {
                // Convert list of TicketType to list of String names
                List<String> ticketTypeNames = ticketTypes.stream()
                        .map(TicketType::getTypeName)
                        .collect(Collectors.toList());

                // Add the names to the ComboBox
                ticketTypeBox.getItems().addAll(ticketTypeNames);
                ticketTypeBox.setDisable(false);
            }
        } catch (Exception e) {
            System.err.println("Failed to load ticket types: " + e.getMessage());
            ticketTypeBox.getItems().add("Error loading ticket types");
            ticketTypeBox.setDisable(true);
        }

        // Set on action handler to open the multi-select dialog
        ticketTypeBox.setOnAction(e -> showTicketTypeSelectionDialog());

    }

    private void showTicketTypeSelectionDialog() {
        List<TicketType> allTicketTypes = new ArrayList<>();

        // Get ticket types from database
        try {
            allTicketTypes = ticketTypeModel.getObservableList();

            // Add this check to prevent trying to show an empty dialog
            if (allTicketTypes.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Ticket Types",
                        "There are no ticket types available. Please create ticket types first.");
                return;
            }
        } catch (Exception e) {
            System.err.println("Could not load ticket types: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not load ticket types: " + e.getMessage(),
                    ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Create dialog for selection
        Dialog<List<TicketType>> dialog = new Dialog<>();
        dialog.setTitle("Select Ticket Types");
        dialog.setHeaderText("Choose ticket types for this event");

        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        List<CheckBox> checkBoxes = new ArrayList<>();

        // Create a checkbox for each ticket type
        for (TicketType type : allTicketTypes) {
            CheckBox cb = new CheckBox(type.getTypeName());
            cb.setUserData(type);

            // Check if this type was previously selected
            if (selectedTicketTypes.stream().anyMatch(t -> t.getTicketTypeID() == type.getTicketTypeID())) {
                cb.setSelected(true);
            }

            checkBoxes.add(cb);
            vbox.getChildren().add(cb);
        }

        dialog.getDialogPane().setContent(vbox);

        // Set minimum dialog size to ensure all content is visible
        dialog.getDialogPane().setMinWidth(300);
        dialog.getDialogPane().setMinHeight(200);

        // Convert the result when dialog closes
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                List<TicketType> selected = checkBoxes.stream()
                        .filter(CheckBox::isSelected)
                        .map(cb -> (TicketType)cb.getUserData())
                        .collect(Collectors.toList());
                return selected;
            }
            return null;
        });

        // Show dialog and process result
        Optional<List<TicketType>> result = dialog.showAndWait();
        result.ifPresent(types -> {
            selectedTicketTypes = types;
            updateTicketTypeSelectionDisplay();
        });
    }

    private void updateTicketTypeSelectionDisplay() {
        if (selectedTicketTypes.isEmpty()) {
            ticketTypeBox.setValue("No ticket types selected");
        } else {
            ticketTypeBox.setValue(selectedTicketTypes.size() + " ticket type(s) selected");
        }
    }


    @FXML
    private void onCreate() {
        try {
            String eventName = newEventName.getText();
            String eventLocation = newEventLocation.getText();
            String eventDescription = newEventInfo.getText();

            // Validate required fields
            if (eventName.isEmpty() || newEventDate.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all required fields.");
                return;
            }

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

            // Check if we have ticket types selected
            if (selectedTicketTypes.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select at least one ticket type.");
                return;
            }

            if (selectedEvent != null) {
                // EDIT mode
                selectedEvent.setEventName(eventName);
                selectedEvent.setLocation(eventLocation);
                selectedEvent.setDescription(eventDescription);
                selectedEvent.setEventDate(eventDate);
                selectedEvent.setNotes(eventNotes);
                selectedEvent.setAvailableTickets(availableTickets);
                selectedEvent.setOptionalInformation(optionalInformation);


                eventModel.updateEvent(selectedEvent);

                // Create tickets for each selected ticket type
                for (TicketType ticketType : selectedTicketTypes) {

                    Tickets ticket = new Tickets(0, ticketType.getTicketTypeID(), selectedEvent.getEventId());


                    // Add the ticket to the database
                    eventModel.addTicketToEvent(ticket);
                }

                // Force manual refresh
                if (coordScreenController != null) {
                    System.out.println("Manual refresh after update");
                    coordScreenController.forceRefresh();
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Event updated successfully!");
            }
            else {
                // Create Mode
                Events newEvent = new Events(0, eventName, eventDescription, eventDate, eventLocation,
                        eventNotes, availableTickets, optionalInformation);

                // Create the event first
                eventModel.createEvent(newEvent);

                int newEventId = newEvent.getEventId();

                for (TicketType ticketType : selectedTicketTypes) {
                    Tickets newTicket = new Tickets(0, ticketType.getTicketTypeID(), newEventId);


                    // Add the ticket to the database
                    eventModel.addTicketToEvent(newTicket);
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Event created successfully!");
            }

            clearInputFields();

            // Refresh the event table in the coordinator screen
            if (coordScreenController != null) {
                coordScreenController.forceRefresh();
            }

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

    public void setCoordScreenController(CoordScreenController coordScreenController) {this.coordScreenController = coordScreenController;}

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
