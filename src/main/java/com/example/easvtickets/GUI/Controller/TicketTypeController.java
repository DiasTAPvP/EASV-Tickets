package com.example.easvtickets.GUI.Controller;


import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.GUI.Model.TicketTypeModel;
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

    public TicketTypeController() throws Exception {
        ticketTypeModel = new TicketTypeModel();
    }

    public void setCoordScreenController(CoordScreenController coordScreenController) {
        this.setCoordScreenController = coordScreenController;
    }

    public void onCreateTicket() throws Exception {
        String typeName = ticketName.getText();
        String description = ticketDescription.getText();

        TicketType newTicketType = new TicketType(0, typeName, description);
        ticketTypeModel.createTicketType(newTicketType);

        //Refresh the ticket table without reloading the entire screen
        /*ticketTable.getItems().clear();
        ticketTable.getItems().addAll(ticketTypeModel.getObservableList());
        ticketName.clear();
        ticketDescription.clear();*/
        ticketTypeModel.refreshTicketTypes();


    }


    public void onDeleteTicket() throws Exception {
        if (selectedTicketType != null) {
            ticketTypeModel.deleteTicketType(selectedTicketType);
            ticketTable.getItems().remove(selectedTicketType);
            selectedTicketType = null;
        }
    }








}
