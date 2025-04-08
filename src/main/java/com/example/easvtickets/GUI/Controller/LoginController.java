package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BLL.UserManager;
import com.example.easvtickets.DAL.DAO.UserDAO;
import com.example.easvtickets.BLL.Util.BCryptUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button coordLogin;
    @FXML
    private Button adminLogin;
    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button togglePasswordButton;
    @FXML
    private TextField loginPasswordVisible;

    private boolean passwordVisible = false;

    private UserManager userManager;

    public LoginController() throws IOException {
        userManager = new UserManager();
    }

    /*@FXML
    //Old methods for test login
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
    //Old methods for test login with password and username testing
    @FXML
    private void onCoordLoginPressed() throws IOException {
        if (login(loginUsername.getText(), loginPassword.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/coordinator-screen.fxml"));
            Parent root = loader.load();

            CoordScreenController coordcontroller = loader.getController();
            coordcontroller.setLoginController(this);

            Stage loginStage = (Stage) coordLogin.getScene().getWindow();
            loginStage.close();

            Stage stage = new Stage();
            stage.setTitle("Welcome Coordinator");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } else {
            // Show error message

        }
    }


    @FXML
    private void onAdminLoginPressed() throws IOException {
            if (login(loginUsername.getText(), loginPassword.getText())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin-screen.fxml"));
                Parent root = loader.load();

                AdminScreenController admincontroller = loader.getController();
                admincontroller.setLoginController(this);

                Stage loginStage = (Stage) adminLogin.getScene().getWindow();
                loginStage.close();

                Stage stage = new Stage();
                stage.setTitle("Welcome Admin");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } else {
            // Handle login failure (e.g., show an error message)
        }
    }*/

    @FXML
    private void onTogglePasswordPressed() {
        passwordVisible = !passwordVisible;

        loginPassword.setVisible(!passwordVisible);
        loginPasswordVisible.setVisible(passwordVisible);
    }

    @FXML
    private void onLoginPressed() throws IOException {
        String username = loginUsername.getText();
        String password = loginPassword.getText();
        Users user = userManager.getUsername(username);

        if (user != null && BCryptUtil.checkPassword(password, user.getPasswordhash())) {
            FXMLLoader loader;
            if (user.getIsadmin()) {
                loader = new FXMLLoader(getClass().getResource("/admin-screen.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/coordinator-screen.fxml"));
            }
            Parent root = loader.load();

            if (user.getIsadmin()) {
                AdminScreenController admincontroller = loader.getController();
                admincontroller.setLoginController(this);
                admincontroller.setUser(user);
            } else {
                CoordScreenController coordcontroller = loader.getController();
                coordcontroller.setLoginController(this);
                coordcontroller.setUser(user);
            }

            Stage loginStage = (Stage) coordLogin.getScene().getWindow();
            loginStage.close();

            Stage stage = new Stage();
            stage.setTitle(user.getIsadmin() ? "Welcome Admin" : "Welcome Coordinator");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password");
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginPasswordVisible.setVisible(false);
        loginPasswordVisible.managedProperty().bind(loginPasswordVisible.visibleProperty());
        loginPasswordVisible.textProperty().bindBidirectional(loginPassword.textProperty());

    }

    public boolean login(String username, String password) {
        Users user = userManager.getUsername(username);
        if (user != null) {
            return BCryptUtil.checkPassword(password, user.getPasswordhash());
        }
        return false;
    }
}