package com.example.easvtickets.GUI.Controller;


import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.Users;
import com.example.easvtickets.DAL.DAO.EventDAO;
import com.example.easvtickets.DAL.DAO.UserDAO;
import com.example.easvtickets.GUI.Model.EventModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.List;
import java.io.IOException;

public class CoordScreenController {

    @FXML private Label infoLabelCoord;
    @FXML private Button ticketsButton;
    @FXML private Button manageEventButton;
    @FXML private Button createEventButton;
    @FXML TableView<Events> personalEventsCoord;
    @FXML private TableView<Events> coordEventTable;
    @FXML private TableColumn<Events, Integer> eventIdColumn;
    @FXML private TableColumn<Events, String> eventNameColumn;
    @FXML private TableColumn<Events, Timestamp> eventDateColumn;
    @FXML private TableView<Users> availableCoordPeopleTable;
    @FXML private TableColumn<Users, String> availableCoordPeopleColumn;
    @FXML private TableColumn<Events, String> eventDescriptionColumn;
    @FXML private TableColumn<Events, String> eventLocationColumn;
    @FXML private TableColumn<Events, Integer> availableTicketsColumn;
    @FXML private TextArea currentEventInfoCoord;
    @FXML private Button coordLogout;

    private EventDAO eventDAO;
    private UserDAO userDAO;

    private EventModel eventModel;

    private Events selectedEvent;



    public CoordScreenController() throws Exception {
        this.eventDAO = new EventDAO();
        this.eventModel = new EventModel();
        this.userDAO = new UserDAO();
    }

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    /**
     * Implement:
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/create-event-view.fxml"));
        Parent root = loader.load();


        Events selectedEvent = coordEventTable.getSelectionModel().getSelectedItem();
        System.out.println("Selected from table: " + selectedEvent);
        System.out.println("Row selected in table? " + coordEventTable.getSelectionModel().isEmpty());
        //Controller for new view
        EventWindowController eventController = loader.getController();

        //Passes selected event to edit
        eventController.setSelectedEvent(selectedEvent);
        //?PleaseUpdate????
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
        EventWindowController eventWindowController = loader.getController();
        eventWindowController.setCoordScreenController(this);

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
        availableCoordPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("username"));


        eventDateColumn.setCellFactory(column -> new TableCell<Events, Timestamp>() {
            @Override
            protected void updateItem(Timestamp eventDate, boolean empty) {
                super.updateItem(eventDate, empty);
                if (empty || eventDate == null) {
                    setText(null);
                } else {
                    // Format as DD/MM/YYYY
                    setText(String.format("%02d/%02d/%d %02d:%02d",
                            eventDate.toLocalDateTime().getDayOfMonth(),
                            eventDate.toLocalDateTime().getMonthValue(),
                            eventDate.toLocalDateTime().getYear(),
                            eventDate.toLocalDateTime().getHour(),
                            eventDate.toLocalDateTime().getMinute()));
                }
            }
        });

        loadEvents();
        loadUsers();

        availableCoordPeopleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Users selectedUser = (Users) newValue;
                System.out.println("Selected User: " + selectedUser.getUsername());
                infoLabelCoord.setText(selectedUser.getUsername());
            }
        });


        coordEventTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEvent = (Events) newValue;
                System.out.println("Selected Event: " + selectedEvent.getEventName());
                infoLabelCoord.setText(selectedEvent.getEventName());
                displayEventDetails(newValue);
            }
        });

    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    public void tableRefresh() {
        System.out.println("Refreshing tables");
        try {
            loadEvents();
            System.out.println("Events table refreshed succesfully");
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try {
            List<Users> allUsers = userDAO.getAllUsers();

            // Filter to only include non-admin users
            List<Users> nonAdminUsers = allUsers.stream()
                    .filter(user -> !user.getIsadmin())
                    .toList();

            ObservableList<Users> usersObservableList = FXCollections.observableArrayList(nonAdminUsers);
            availableCoordPeopleTable.setItems(usersObservableList);
        } catch (Exception e) {
            e.printStackTrace();
            infoLabelCoord.setText("Error loading users: " + e.getMessage());
        }
    }

    private void loadEvents() {
        try {
            eventModel.refreshEvents();
            coordEventTable.setItems(eventModel.getObservableList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayEventDetails(Events event) {
        StringBuilder details = new StringBuilder();
        details.append("Event: ").append(event.getEventName()).append("\n");
        details.append("Description: ").append(event.getDescription()).append("\n");
        details.append("Date: ").append(event.getEventDate()).append("\n");
        details.append("Location: ").append(event.getLocation()).append("\n");
        details.append("Notes: ").append(event.getNotes()).append("\n");
        details.append("Available Tickets: ").append(event.getAvailableTickets()).append("\n");
        details.append("Optional Information: ").append(event.getOptionalInformation()).append("\n");

        currentEventInfoCoord.setText(details.toString());
    }

    public void forceRefresh() {
        Platform.runLater(() -> {
            try {
                // Load fresh data
                eventModel.refreshEvents();
                coordEventTable.setItems(eventModel.getObservableList());
                coordEventTable.refresh();
                System.out.println("TableView refreshed with " + eventModel.getObservableList().size() + " items");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void onDeleteButtonPressed() throws Exception {

        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this event?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            Events selectedEvent = coordEventTable.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                eventModel.deleteEvents(selectedEvent);
                coordEventTable.getItems().remove(selectedEvent);
                System.out.println("Event deleted succesfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No event selected. Please select an event to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("No event selected.");

            }
        }
    }

    public Events getSelectedEvent() {
        return selectedEvent;
    }

    @FXML
    private void onPersonInfoButtonPressed(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/personal-info.fxml"));
        Parent root = loader.load();
        PersonalInfoController personalInfoController = loader.getController();
        personalInfoController.setUser(currentUser);


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Personal Information");
        stage.show();
    }



    @FXML
    private void onLogoutPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-form.fxml"));
        Parent root = loader.load();

        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

        //Closes current window
        Stage currentStage = (Stage) coordLogout.getScene().getWindow();
        currentStage.close();
    }
    private Users currentUser;
    public void setUser(Users user) {
        currentUser = user;
    }
}
