package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;

//import java.awt.event.ActionEvent;

public class PersonalInfoController {
    @FXML private Label usernameLabel;




    @FXML
    private void onChangePassButtonPressed(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/change-password.fxml"));
        Parent root = loader.load();
        ChangePassController changePassController = loader.getController();
        changePassController.setUser(currentUser);

        Stage stage= new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Change password");
        stage.show();
    }
    private Users currentUser;
    public void setUser(Users user) {
        currentUser = user;
    }

@FXML
public void initialize() {
    //Set the label to the current user
    usernameLabel.setText("Bo");
    //usernameLabel.setText(currentUser.getUsername());

}
}

