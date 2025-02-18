package com.example.easvtickets;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TicketApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EASV Tickets");
        stage.setScene(scene);
        stage.show();
    } /*catch (IOException e) {
        e.printStackTrace();
    }*/

    public static void main(String[] args) {
        launch();
    }
}