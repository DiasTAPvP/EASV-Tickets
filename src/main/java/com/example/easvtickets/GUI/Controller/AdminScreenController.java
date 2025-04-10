package com.example.easvtickets.GUI.Controller;


import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.Tickets;
import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BLL.UserManager;
import com.example.easvtickets.DAL.DAO.EventDAO;
import com.example.easvtickets.DAL.DAO.UserDAO;
import com.example.easvtickets.GUI.Model.EventModel;
import com.example.easvtickets.GUI.Model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminScreenController {

    @FXML private Button manageEntityButton;
    @FXML private TableView<Users> assignedAdminCoordPeopleTable;
    @FXML private TableColumn<Users, String> assignedAdminCoordPeopleColumn;
    @FXML private TableView<Events> eventTableAdmin;
    @FXML private TableView<Users> userTableAdmin;
    @FXML private TableColumn<Events, Integer> eventIdColumn;
    @FXML private TableColumn<Events, String> eventNameColumn;
    @FXML private TableColumn<Events, Timestamp> eventDateColumn;
    @FXML private TableColumn<Events, String> adminPeopleColumn;
    @FXML private TableColumn<Events, String> eventDescriptionColumn;
    @FXML private TableColumn<Events, String> eventLocationColumn;
    @FXML private TableColumn<Events, Integer> availableTicketsColumn;
    @FXML private TextArea entityInfoAdmin;
    @FXML private Button adminLogout;
    @FXML private Button createUserButton;
    @FXML private Label infoTextAdmin;

    private EventDAO eventDAO;
    private UserDAO userDAO;
    private EventModel eventModel;
    private UserModel userModel;
    private Events selectedEvent;
    private Users selectedUser;

    public AdminScreenController() throws Exception {
        this.eventDAO = new EventDAO();
        this.userDAO = new UserDAO();
        this.eventModel = new EventModel();
        this.userModel = new UserModel();
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

    public void refreshUserTable() {
        System.out.println("Table refreshed");
        try {
            userModel.refreshUsers();
            ObservableList<Users> currentUsers = userModel.getObservableList();
            userTableAdmin.setItems(currentUsers);
            System.out.println("Number of events in ObservableList: " + currentUsers.size());

        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }

    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }


    @FXML
    private void onManageEntityButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin- .fxml"));
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
    private void onCreateUserButtonPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/create-user.fxml"));
        Parent root = loader.load();

        CreateUserController createUserController = loader.getController();
        createUserController.setAdminScreenController(this);

        Stage stage = new Stage();
        stage.setTitle("Create New Users");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    public void initialize() {

        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        adminPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        assignedAdminCoordPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

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
        setupDragAndDrop();
        setupRemoveCoordinatorContextMenu();

        userTableAdmin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Users selectedUser = (Users) newValue;
                System.out.println("Selected User: " + selectedUser.getUsername());
                infoTextAdmin.setText(selectedUser.getUsername());
            }
        });

        eventTableAdmin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEvent = (Events) newValue;
                System.out.println("Selected Event: " + selectedEvent.getEventName());
                infoTextAdmin.setText(selectedEvent.getEventName());
                displayEventDetails(newValue);

                // Load assigned coordinators for the selected event
                loadAssignedCoordinators(selectedEvent.getEventId());
            }
        });


    }

    private void loadUsers() {
        try {
            List<Users> usersList = userDAO.getAllUsers();
            ObservableList<Users> usersObservableList = FXCollections.observableArrayList(usersList);
            userTableAdmin.setItems(usersObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        StringBuilder details = new StringBuilder();
        details.append("Event: ").append(event.getEventName()).append("\n");
        details.append("Description: ").append(event.getDescription()).append("\n");
        details.append("Date: ").append(event.getEventDate().toLocalDateTime().format(formatter)).append("\n");
        details.append("Location: ").append(event.getLocation()).append("\n");
        details.append("Notes: ").append(event.getNotes()).append("\n");
        details.append("Available Tickets: ").append(event.getAvailableTickets()).append("\n");

        entityInfoAdmin.setText(details.toString());
    }

    @FXML
    private void onAdminDeleteButtonPressed() throws Exception {
        //Implement functionality
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the event or coordinator?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            Events selectedEvent = eventTableAdmin.getSelectionModel().getSelectedItem();
            Users selectedUser = userTableAdmin.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                eventModel.deleteEvents(selectedEvent);
                eventTableAdmin.getItems().remove(selectedEvent);
                System.out.println("Event deleted succesfully.");

            } else if (selectedUser != null) {
                userModel.deleteUsers(selectedUser);
                userTableAdmin.getItems().remove(selectedUser);
                System.out.println("User deleted succesfully.");

            } else {
                JOptionPane.showMessageDialog(null, "No event or coordinator selected. Please select an event to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("No event or user selected.");

            }
        }
    }

    private void setupDragAndDrop() {
        // Enable drag from available coordinators
        userTableAdmin.setOnDragDetected(event -> {
            if (userTableAdmin.getSelectionModel().getSelectedItem() != null) {
                Dragboard db = userTableAdmin.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(userTableAdmin.getSelectionModel().getSelectedItem().getLoginid()));
                db.setContent(content);
                event.consume();
            }
        });

        // Enable drop on assigned coordinators
        assignedAdminCoordPeopleTable.setOnDragOver(event -> {
            if (event.getGestureSource() != assignedAdminCoordPeopleTable &&
                    event.getDragboard().hasString() &&
                    selectedEvent != null) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        assignedAdminCoordPeopleTable.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString() && selectedEvent != null) {
                try {
                    int userId = Integer.parseInt(db.getString());
                    Users draggedUser = userTableAdmin.getItems().stream()
                            .filter(user -> user.getLoginid() == userId)
                            .findFirst()
                            .orElse(null);

                    if (draggedUser != null) {
                        // Assign the user to the event
                        userDAO.assignCoordinatorToEvent(userId, selectedEvent.getEventId());

                        // Refresh the assigned coordinators table
                        loadAssignedCoordinators(selectedEvent.getEventId());
                        success = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    infoTextAdmin.setText("Error assigning coordinator: " + e.getMessage());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupRemoveCoordinatorContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove Coordinator");

        removeItem.setOnAction(event -> {
            Users selectedUser = assignedAdminCoordPeopleTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null && selectedEvent != null) {
                try {
                    userDAO.removeCoordinatorFromEvent(selectedUser.getLoginid(), selectedEvent.getEventId());
                    loadAssignedCoordinators(selectedEvent.getEventId());
                    infoTextAdmin.setText("Coordinator removed successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    infoTextAdmin.setText("Error removing coordinator: " + e.getMessage());
                }
            }
        });

        contextMenu.getItems().add(removeItem);
        assignedAdminCoordPeopleTable.setContextMenu(contextMenu);
    }

    private void loadAssignedCoordinators(int eventId) {
        try {
            List<Users> assignedCoordinators = userDAO.getAssignedCoordinators(eventId);
            ObservableList<Users> coordinatorsObservableList = FXCollections.observableArrayList(assignedCoordinators);
            assignedAdminCoordPeopleTable.setItems(coordinatorsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
            infoTextAdmin.setText("Error loading assigned coordinators: " + e.getMessage());
        }
    }

    @FXML
    private void onCreateUserButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/create-user.fxml"));
        Parent root = loader.load();

        Stage stage =  new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Create User");
        stage.show();
    }

    @FXML
    private void onAdminPersonInfoButtonPressed(ActionEvent event) throws IOException {

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
    private void onAdminLogoutPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-form.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

        //Closes current window
        Stage currentStage = (Stage) adminLogout.getScene().getWindow();
        currentStage.close();
    }
    private Users currentUser;
    public void setUser(Users user) {
        currentUser = user;
    }
}
