package com.example.easvtickets.GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button coordLogin;
    @FXML
    private Button adminLogin;

    @FXML
    private void onCoordLoginPressed() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/coordinator-screen.fxml"));
        Parent root = loader.load();

        //Get the controller and set the controller
        CoordScreenController coordcontroller = loader.getController();
        coordcontroller.setLoginController(this);

        Stage loginStage = (Stage) coordLogin.getScene().getWindow();
        loginStage.close();

        Stage stage = new Stage();
        stage.setTitle("Welcome Coordinator");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    @FXML
    private void onAdminLoginPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin-screen.fxml"));
        Parent root = loader.load();

        //Get the controller and set the controller
        AdminScreenController admincontroller = loader.getController();
        admincontroller.setLoginController(this);

        Stage loginStage = (Stage) adminLogin.getScene().getWindow();
        loginStage.close();

        Stage stage = new Stage();
        stage.setTitle("Welcome Admin");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //Implement login functionality much later


}