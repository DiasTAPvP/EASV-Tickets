package com.example.easvtickets.GUI.Controller;


import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.BLL.TicketTypeManager;
import com.example.easvtickets.GUI.Model.TicketTypeModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TicketTypeController {



    private CoordScreenController setCoordScreenController;

    @FXML private TextField ticketName;
    @FXML private TextArea ticketDescription;
    @FXML private Button addTicketButton;
    @FXML private Button deleteTicketButton;
    @FXML private TableView<TicketType> ticketTable;
    @FXML private TableColumn<TicketType, String> ticketTableCol;


    private TicketType selectedTicketType;
    private TicketTypeModel ticketTypeModel;
    private TicketTypeManager ticketTypeManager;

    public TicketTypeController() throws Exception {
        ticketTypeModel = new TicketTypeModel();
        ticketTypeManager = new TicketTypeManager();
    }

    public void setCoordScreenController(CoordScreenController coordScreenController) {
        this.setCoordScreenController = coordScreenController;
    }

    public void onCreateTicket() throws Exception {
        String typeName = ticketName.getText();
        String description = ticketDescription.getText();

        if (typeName.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ticket name cannot be empty.");
            alert.showAndWait();
            return;
        }

        TicketType newTicketType = new TicketType(0, typeName, description);
        ticketTypeModel.createTicketType(newTicketType);

        //Refresh the ticket table
        ticketName.clear();
        ticketDescription.clear();

        loadTickets();


    }


    public void onDeleteTicket() throws Exception {
        if (selectedTicketType != null) {
            try {
                ticketTypeModel.deleteTicketType(selectedTicketType);
                // Refresh ticket table
                loadTickets();
                // Clear fields
                ticketName.clear();
                ticketDescription.clear();
                selectedTicketType = null;
            } catch (Exception e) {
                String msg = e.getMessage();
                if (e.getMessage().contains("Cannot be deleted")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Unexpected error has occured.");
                    alert.showAndWait();
                } else {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Ticket type cannot be deleted because it's currently being used by a customer for an event.");
                    alert.showAndWait();
                }
            }
        }   else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No ticket type selected.");
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        System.out.println("TicketTypeController initialized");

        if (ticketTableCol == null) {
            System.err.println("TicketTableCol not properly connected to FXML");
            return;
        }

        ticketTableCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTypeName()));

        ticketTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTicketType = newValue;
                //ticketTableCol.setText(selectedTicketType.getTypeName());
                ticketTable.setItems(ticketTypeModel.getObservableList());

            }
        });

        try {
            loadTickets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTickets() throws Exception {
        ticketTypeModel.refreshTicketTypes();
        ticketTable.setItems(ticketTypeModel.getObservableList());
    }








}
