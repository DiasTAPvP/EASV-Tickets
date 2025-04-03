package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Users;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;




public class ChangePassController {

    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Button passSaveButton;

    @FXML
    private void OnSavePasswordPressed(ActionEvent event) {
        String current = currentPassword.getText();
        String newPass = newPassword.getText();
        String confirmPass = confirmPassword.getText();

    }
    private Users currentUser;
    public void setUser(Users user) {
        currentUser = user;
        passSaveButton.setText(user.getUsername());
    }
}
