package com.cmpt370.timetracker;

import com.cmpt370.timetracker.JNI;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ScheduledTimeBlock Class
 * Responsible for storing a time block
 */
public class ScheduledTimeBlock extends Timer implements Serializable {

    //start time of time block, stored as seconds in 24 time (86,400 represents 24 hours in the day)
    private int startTime;

    //end time of time block, stored as seconds in 24 time (86,400 represents 24 hours in the day)
    private int endTime;

    //name of block
    private int totalWorkTime;

    //elapsed time for the block
    private String tagName;

    //description for time block
    private String Description;


    //secondary total worked time variable
    private int total_worked_time;

    transient private Label task_timer;

    //track applications used during the block
    AppTracker tracker = new AppTracker();

    //constructor
    public ScheduledTimeBlock(){
        super();
        this.startTime = 0;
        this.endTime = 0;
        this.totalWorkTime = 0;
        this.tagName = "filler";
        this.Description = "filler";
        this.total_worked_time = 0;
    };

    //getters and setters for the above variables
    public AppTracker getAppTracker() {
        return tracker;
    }

    public void setTask_timer(Label task_timer) {
        this.task_timer = task_timer;
    }

    public int getStartTime(){ return startTime; }

    public int getendTime(){ return endTime; }

    public int gettotalWorkTime(){ return totalWorkTime; }

    public String gettagName(){ return tagName; }

    public String getDescription(){ return Description; }

    public void setStartTime(int st) {
        startTime = st;
    }

    public void setEndTime(int et) {
        endTime = et;
    }

    public void setTotalWorkTime(int ttl) { totalWorkTime = ttl; }

    public void setTagName(String tn) { tagName = tn; }

    public void setDescription(String desc) { Description = desc; }

    public int getTotalWorkedTime(){ return this.total_worked_time; }


    /**
     * timetostr function
     * Responsible for converting the time given into a string (in 24 hour format)
     * @param seconds - seconds to be converted
     * @return - formatted time in string
     */
    public String timetostr(int seconds){
        int hr = seconds / 3600;
        int min = (seconds % 3600) / 60;
        int sec = (seconds % 3600) % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hr, min, sec);

        return formattedTime;
    }

    /**
     * strtotime function
     * Responsible for converting a given string of time (in 24 hour format) into seconds
     * @param time
     * @return
     */
    private int strtotime(String time) {
        //split the string into hr min sec via ":"
        String[] parts = time.split(":");

        //handle invalid input
        if (parts.length != 3) { throw new IllegalArgumentException("Invalid time format. Use '00:00:00'"); }

        //convert components into integers
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        //handle invalid time inputs
        if (hours < 0 || minutes < 0 || seconds < 0) { throw new IllegalArgumentException("Time components cannot be negative"); }

        //return seconds
        return hours * 3600 + minutes * 60 + seconds;
    }

    /**
     * total block duration
     * calculate the total block duration
     * @return total block duration
     */
    public double totalBlockDuration() {
        return endTime - startTime;
    }

    /**
     * totalRecordedTime
     * calculate total elapsed recorded time
     * @return total elapsed time
     */
    public double totalRecordedTime() {
        return totalBlockDuration() - JNI.get().get().CurrentIdleTime();
    }

    /**
     * timeRemaining
     * calculate time remaining in the time block (from elapsed time)
     */
    public int timeRemaining() {
        return endTime - strtotime(this.getCurrentTime());
    }

    /**
     * stop
     * stop the timer and store into variables and adjust variables accordingly
     */
    public void stop(){
        super.stop();
        this.total_worked_time = this.total_time_work();
        this.task_timer.setText(this.timetostr(this.total_worked_time));
        System.out.println("total worked time: " + this.total_worked_time);
    }




    /**
     * returnElapsedTime
     * return the total elapsed time of the block
     * @return formatted elapsed time
     */
    public String returnElapsedTime() {
        String formatted = String.format("%02d:%02d:%02d:%02d", this.day, this.hour, this.minute , this.second);
        return formatted;
    }
}
