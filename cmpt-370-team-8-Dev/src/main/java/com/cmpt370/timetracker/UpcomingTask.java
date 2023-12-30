package com.cmpt370.timetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UpcomingTask extends AnchorPane {
    Model model;

    ScheduledTimeBlock block = null;
    LocalDate date;

    @FXML
    Spinner startHour;
    @FXML
    Spinner startMin;

    @FXML
    Spinner endHour;
    @FXML
    Spinner endMin;

    @FXML
    Button editButton;

    @FXML
    Button CancelButton;

    @FXML
    Label task_timer;

    @FXML
    Button charts;

    @FXML
    TextField NewTaskName;

    boolean delete = false;
    Stage appChartInfo;
    AppChartController chartController;

    private Notification notification;

    private ArrayList<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private boolean listenerAdded = false;

    public UpcomingTask(Model model, LocalDate date) {
        this.date = date;
        this.model = model;
        this.notification = new Notification();
        addDataChangeListener(() -> {
            notification.alertMessage("Task Saved", "The task data has been successfully modified.");
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "New-Task.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public UpcomingTask(Model model, LocalDate date, ScheduledTimeBlock block) {
        this.date = date;
        this.model = model;
        this.block = block;

        addDataChangeListener(() -> {
            notification.alertMessage("Task Saved", "The task data has been successfully modified.");
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "New-Task.fxml"));
        block.setTask_timer(task_timer);
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        startHour.getValueFactory().setValue(block.getStartTime() / 3600);
        startMin.getValueFactory().setValue((block.getStartTime() % 3600) / 60);
        endHour.getValueFactory().setValue(block.getendTime() / 3600);
        endMin.getValueFactory().setValue((block.getStartTime() % 3600) / 60);
        this.task_timer.setText(this.block.timetostr(this.block.total_time_work()));
    }

    @FXML
    public void initialize() {
        // Only display the chart button if this is a valid timeblock thats been submitted
        if (this.block != null) {
            this.charts.setDisable(false);
            this.block.setTask_timer(this.task_timer);
            this.NewTaskName.setText(this.block.gettagName());
        }
    }

    @FXML
    void chartsOnPressed(ActionEvent event) {
        try {
            // Setup App chart window
            FXMLLoader appChartFXMLLoader = new FXMLLoader(HelloApplication.class.getResource("AppChart.fxml"));
            this.chartController = new AppChartController(this.model, date, this.block);
            appChartFXMLLoader.setController(this.chartController);
            Scene chartScene = new Scene(appChartFXMLLoader.load(), 600, 400);
            appChartInfo = new Stage();
            appChartInfo.setTitle("Application Data");
            appChartInfo.setScene(chartScene);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.appChartInfo.showAndWait();
    }

    @FXML
    void onEditOrCreate(ActionEvent event) {
        int startHourSec = (int) startHour.getValue();
        int startMinSec = (int) startMin.getValue();
        int endHourSec = (int) endHour.getValue();
        int endMinSec = (int) endMin.getValue();

        int startTime = startHourSec * 60 * 60 + startMinSec * 60;
        int endTime = endHourSec * 60 * 60 + endMinSec * 60;

        System.out.println(startTime);
        System.out.println(endTime);
        if (startTime >= endTime) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input");
            alert.showAndWait();
            return;
        }

        if (notification == null) {
            notification = new Notification();
        }

        if (block == null) {
            notification.alertMessage("New Task Created", "New Task successfully created");
//            Alert alert = new Alert(Alert.AlertType.INFORMATION, "New Task successfully created");
//            alert.setTitle("Success");
//            alert.showAndWait();
            Day day = model.getSchedule().getDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
            // TODO: New time block
            this.block = day.newTimeBlock(startTime, endTime, NewTaskName.getText());
            this.charts.setDisable(false);
            listenerAdded = true;
            this.block.setTask_timer(task_timer);
        } else {

            if (!listenerAdded) {
                addDataChangeListener(() -> {
                    notification.alertMessage("Task Saved", "The task data has been successfully modified.");
                });
                listenerAdded = true;
            }
            notifyDataChanged();
            block.setStartTime(startTime);
            block.setEndTime(endTime);
            block.setTagName(NewTaskName.getText());
            block.setTask_timer(task_timer);

        }
    }

    public boolean getDelete() {
        return this.delete;
    }

    @FXML
    void CancelButtonAction(ActionEvent event) {

//        ButtonType ok = new ButtonType("OK");
        delete = true;
        listenerAdded = false;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to cancel?");
        a.showAndWait();
//
//        System.out.println(ok);
//        System.out.println(a.getResult().toString());
//        System.out.println(a.getResult().getText());
        if(a.getResult().getText().equals("OK"))
        {
            if(block != null)
            {
                model.getSchedule().getDay(date.getDayOfMonth(), date.getMonthValue(), date.getYear()).removeTimeBlock(block.gettagName());
                startHour.getValueFactory().setValue(0);
                startMin.getValueFactory().setValue(0);
                endHour.getValueFactory().setValue(0);
                endMin.getValueFactory().setValue(0);
                task_timer.setText("00:00:00");

                NewTaskName.setText("");
                this.getChildren().clear();
            }
        }
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public void addDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

}
