package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BLL.UserManager;
import com.example.easvtickets.BLL.Util.BCryptUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;

public class CreateUserController {
    
    @FXML private TextField newUserName;
    @FXML private PasswordField newUserPassword;
    @FXML private Button saveNewUserButton, userCancelButton;
    @FXML private CheckBox makeAdminCheck;
    
    private UserManager userManager;
    private AdminScreenController adminScreenController;

    
    public CreateUserController() throws IOException {
        userManager = new UserManager();
    }
    
    public void setAdminScreenController(AdminScreenController adminScreenController) {this.adminScreenController = adminScreenController;}


    //Method to create a new user
    @FXML
    public void onCreateUser() throws Exception {
        String username = newUserName.getText();
        String password = newUserPassword.getText();
        boolean isAdmin = false;
        String hashedPassword = BCryptUtil.hashPassword(password);
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        
        Users newUser = new Users(0, username, hashedPassword, isAdmin, createdAt);
        userManager.createUser(newUser);

        //Close the window
        Stage stage = (Stage) saveNewUserButton.getScene().getWindow();
        stage.close();

        //Refresh the user table in the admin screen
        if (adminScreenController != null) {
            adminScreenController.refreshUserTable();
        }

    }


    @FXML
    public void onCancelButtonPressed() {
        Stage stage = (Stage) userCancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void generatePassword() {
        String generatedPassword = generateRandomPassword();
        newUserPassword.setText(generatedPassword);
    }

    private String generateRandomPassword() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }


}
