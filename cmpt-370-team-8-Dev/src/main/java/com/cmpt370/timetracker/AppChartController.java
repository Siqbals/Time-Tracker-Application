package com.cmpt370.timetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class AppChartController {
    ScheduledTimeBlock ref;

    Model model;
    LocalDate date;

    @FXML
    Label dateLabel;

    @FXML
    BarChart chart;
    XYChart.Series series = new XYChart.Series();

    @FXML
    Button refresh;

    public AppChartController(Model model, LocalDate date, ScheduledTimeBlock ref) {
        this.ref = ref;
        this.model = model;
        this.date = date;
    }

    @FXML
    public void onRefreshPressed(ActionEvent event) {
        this.UpdateChart();
    }

    private void UpdateChart() {
        // Clear the chart, then construct a new chart from
        // the data stored in the AppTracker
        chart.getData().clear();
        series.getData().clear();
        this.ref.getAppTracker().map.forEach((key, value) -> {
            series.getData().add(new XYChart.Data<>(key, value));
        });
        chart.getData().add(series);
    }

    @FXML
    public void initialize() {
        this.dateLabel.setText(date.toString());
        this.UpdateChart();
    }
}
