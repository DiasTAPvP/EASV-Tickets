package com.example.easvtickets.GUI.Controller;


import com.example.easvtickets.BE.Events;
import com.example.easvtickets.DAL.DAO.EventDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;
import java.io.IOException;

public class CoordScreenController {

    @FXML private Button ticketsButton;
    @FXML private Button manageEventButton;
    @FXML private Button createEventButton;
    @FXML private TableView<Events> allEventsCoord;
    @FXML private TableColumn<Events, Integer> eventIdColumn;
    @FXML private TableColumn<Events, String> eventNameColumn;
    @FXML private TableColumn<Events, Timestamp> eventDateColumn;
    @FXML private TableColumn<Events, String> eventDescriptionColumn;
    @FXML private TableColumn<Events, String> eventLocationColumn;
    @FXML private TableColumn<Events, Integer> availableTicketsColumn;
    @FXML private TextArea currentEventInfoCoord;

    private EventDAO eventDAO;

    public CoordScreenController() throws IOException {
        this.eventDAO = new EventDAO();
    }

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    /**Implement:
     * Table refreshes.
     * Initialize to set up tables on boot
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

    @FXML
    public void initialize() {
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

        loadEvents();

        allEventsCoord.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayEventDetails(newValue);
            }
        });
    }

    private void loadEvents() {
        try {
            List<Events> eventsList = eventDAO.getAllEvents();
            ObservableList<Events> eventsObservableList = FXCollections.observableArrayList(eventsList);
            allEventsCoord.setItems(eventsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayEventDetails(Events event) {
        StringBuilder details = new StringBuilder();
        details.append("Event Name: ").append(event.getEventName()).append("\n");
        details.append("Description: ").append(event.getDescription()).append("\n");
        details.append("Date: ").append(event.getEventDate()).append("\n");
        details.append("Location: ").append(event.getLocation()).append("\n");
        details.append("Notes: ").append(event.getNotes()).append("\n");
        details.append("Available Tickets: ").append(event.getAvailableTickets()).append("\n");
        details.append("Optional Information: ").append(event.getOptionalInformation()).append("\n");

        currentEventInfoCoord.setText(details.toString());
    }
}
