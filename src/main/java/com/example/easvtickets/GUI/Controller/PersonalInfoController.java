package com.example.easvtickets.GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;

//import java.awt.event.ActionEvent;

public class PersonalInfoController {

    @FXML
    public void initialize() {
    }

    @FXML
    private void onChangePassButtonPressed(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/change-password.fxml"));
        Parent root = loader.load();

        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Change password");
        stage.show();
    }
}
