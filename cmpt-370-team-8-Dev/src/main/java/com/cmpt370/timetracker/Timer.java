package com.cmpt370.timetracker;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Timer implements Serializable
{
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;
    transient protected Timeline timeline;

    private boolean paused = false;

    /**
     * Checks if the timer is currently paused.
     *
     * @return True if the timer is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the paused status of the timer.
     *
     * @param paused The new paused status to set.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Constructor for the Timer class. Initializes the timer with zero values and creates a timeline.
     */
    public Timer() {
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.timeline = createTimeline();
    }

    /**
     * Starts the timer.
     */
    public void start()
    {
        this.timeline.play();

    }

    /**
     * Stops the timer.
     */
    public void stop()
    {
        this.timeline.pause();
    }

    /**
     * Checks if the timer is currently running.
     *
     * @return True if the timer is running, false otherwise.
     */
    public boolean isRunning()
    {
        return (this.timeline.getStatus().equals(Animation.Status.RUNNING));
    }

    /**
     * Retrieves the current system time in the format HH:mm:ss.
     *
     * @return A string representing the current system time.
     */
    public String getCurrentTime()
    {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateFormat);
    }

    /**
     * Creates and configures a new Timeline for the timer.
     *
     * @return The newly created Timeline.
     */
    private Timeline createTimeline() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    /**
     * Updates the timer's time values every second.
     */
    private void updateTime()
    {
        this.second++;
        if(second >= 60)
        {
            this.minute++;
            this.second = 0;
        }
        if(minute >= 60)
        {
            this.hour++;
            this.minute = 0;
        }
        if(hour >= 24)
        {
            this.day++;
            this.hour = 0;
        }
    }

    /**
     * Calculates the total elapsed time in seconds.
     *
     * @return The total time in seconds.
     */
    public int total_time_work()
    {
        return this.day * 86400 + this.hour * 3600 + this.minute * 60 + this.second;
    }

    /**
     * Retrieves the current system time in seconds.
     *
     * @return The current system time in seconds.
     */
    public int getCurrentTimeInSecond()
    {
        LocalDateTime now = LocalDateTime.now();
        return now.getSecond() + now.getMinute() * 60 + now.getHour() * 3600;
    }

    /**
     * Custom deserialization for the Timer class to reinitialize the transient timeline.
     *
     * @param in The ObjectInputStream to read from.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        this.timeline = createTimeline();
    }
}

