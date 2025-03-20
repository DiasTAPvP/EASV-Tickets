package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Users;
import com.example.easvtickets.BLL.UserManager;
import com.example.easvtickets.DAL.UserDAO;
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

    private UserManager userManager;

    public LoginController() throws IOException {
        userManager = new UserManager();
    }

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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public boolean login(String username, String password) {
        Users user = userManager.getUsername(username);
        if (user != null) {
            return BCryptUtil.checkPassword(password, user.getPasswordhash());
        }
        return false;
    }
}