package com.cmpt370.timetracker;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;

import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.List;

public class TaskDistributionController {

    Model model;

    public void starts(Stage stage, Day day) throws NoSuchFileException {
        Scene scene = new Scene(new Group());
        stage.setTitle("Daily Task Distribution");
        stage.setWidth(600);
        stage.setHeight(600);

        List<ScheduledTimeBlock> timeblocks = TaskTracker.getTimeBlocksCurrDay(day);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (ScheduledTimeBlock block: timeblocks) {
            String tagname = block.gettagName();
            Double duration = block.totalBlockDuration();
            pieChartData.add(new PieChart.Data(tagname, duration));
        }
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Daily Task Distribution");

        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();
    }



    /**
    public static void main(String[] args) {
        launch(args);
    }
     **/
}