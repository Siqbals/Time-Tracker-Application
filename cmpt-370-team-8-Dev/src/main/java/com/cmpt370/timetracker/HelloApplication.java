package com.cmpt370.timetracker;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HelloApplication extends Application {
    HelloController controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Time Tracking APP");
        stage.setScene(scene);
        stage.show();

        controller = (HelloController)fxmlLoader.getController();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar obj = Calendar.getInstance();
        String currDate = formatter.format(obj.getTime());
        System.out.println("Current Date: "+currDate);
    }

    @FXML
    public void stop() {
        controller.SaveData();
    }

    public static void main(String[] args) {
        launch();
    }
}