package com.cmpt370.timetracker;

import javafx.scene.chart.PieChart;
import javafx.util.Pair;

import java.io.Serializable;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.util.HashMap;
import java.io.Serializable;
import java.util.List;

public class TaskTracker implements Serializable {

    static public List<ScheduledTimeBlock> getTimeBlocksCurrDay(Day day) throws NoSuchFileException {
        return day.getAllTimeBlocks();
    }

    public static void main(String[] args) throws NoSuchFileException {
        TaskTracker test = new TaskTracker();

        //System.out.println(test.getTimeBlocksCurrDay());
    }
}
