package com.example.easvtickets.GUI.Controller;


import com.example.easvtickets.BE.Events;
import com.example.easvtickets.DAL.DAO.EventDAO;
import com.example.easvtickets.DAL.DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class AdminScreenController {

    @FXML private Button manageEntityButton;
    @FXML private TableView<Events> eventTableAdmin;
    @FXML private TableView<Users> userTableAdmin;
    @FXML private TableColumn<Events, Integer> eventIdColumn;
    @FXML private TableColumn<Events, String> eventNameColumn;
    @FXML private TableColumn<Events, Timestamp> eventDateColumn;
    @FXML private TableColumn<Events, String> eventDescriptionColumn;
    @FXML private TableColumn<Events, String> eventLocationColumn;
    @FXML private TableColumn<Events, Integer> availableTicketsColumn;
    @FXML private TextArea entityInfoAdmin;

    private EventDAO eventDAO;
    private UserDAO userDAO;

    public AdminScreenController() throws IOException {
        this.eventDAO = new EventDAO();
        this.userDAO = new UserDAO();
    }

    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    /**Implement:
    * Table Refreshes
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

    @FXML
    public void initialize() {

        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));


        loadEvents();

        eventTableAdmin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayEventDetails(newValue);
            }
        });


    }

    private void loadEvents() {
        try {
            List<Events> eventsList = eventDAO.getAllEvents();
            ObservableList<Events> eventsObservableList = FXCollections.observableArrayList(eventsList);
            eventTableAdmin.setItems(eventsObservableList);
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

        entityInfoAdmin.setText(details.toString());
    }

}
