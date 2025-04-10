package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BLL.TicketTypeManager;
import com.example.easvtickets.DAL.DAO.TicketTypeDAO;
import com.example.easvtickets.GUI.Model.TicketTypeModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.easvtickets.BE.TicketType;
import java.util.ArrayList;
import java.util.List;


public class TicketTypeController {

    @FXML
    private TableView<TicketType> ticketTable;
    @FXML
    private TableColumn<TicketType, String> ticketTableColumn;

    private final ObservableList<TicketType> ticketList = FXCollections.observableArrayList();

    private CoordScreenController setCoordScreenController;

    private TicketTypeDAO ticketTypeDAO;
    private TicketTypeManager ticketTypeManager;
    private TicketTypeModel ticketTypeModel;
    private TicketType selectedTicketType;

    public TicketTypeController() throws Exception {
        ticketTypeManager = new TicketTypeManager();
        ticketTypeDAO = new TicketTypeDAO();
        ticketTypeModel = new TicketTypeModel();
    }

    public void setCoordScreenController(CoordScreenController coordScreenController) {
        this.setCoordScreenController = coordScreenController;
    }

    @FXML
    public void initialize() {
        ticketTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTypeName()));

        ticketTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
              selectedTicketType = newValue;
              ticketTableColumn.setText(selectedTicketType.getTypeName());

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
