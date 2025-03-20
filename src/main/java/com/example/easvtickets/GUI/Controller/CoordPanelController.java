package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Events;
import javafx.event.Event;
import javafx.fxml.FXML;

import javax.swing.*;

public class CoordPanelController {

    private CoordScreenController setCoordScreenController;

    public void setCoordScreenController(CoordScreenController coordScreenController) {
        {
            this.setCoordScreenController = coordScreenController;
        }
    }

    /**
     * Implement:
     * Table refreshes for the user/coordinator tables
     * Some sort of listener to display the info of whatever event was selected
     * The ability to edit the info of the selected event
     * The ability to select a user and assign them as collaborators of your chosen event
     * Delete button functionality
     * Save button functionality
     */

    public Events getselectedEvent() {
        return selectedEvent;
    }


    @FXML
    private void onCordDeleteButtonPressed() throws Exception {
        //Implement functionality
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this event?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            Events selectedEvent = (Events) personalEventsCoord.getSelectionModel().getSelectedItem();

        }
    }
}
