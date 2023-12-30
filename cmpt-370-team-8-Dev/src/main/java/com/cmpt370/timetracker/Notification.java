package com.cmpt370.timetracker;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;
import org.controlsfx.control.Notifications;


import java.util.HashMap;
import java.util.Objects;

public class Notification
{
    Timer currentTime;

    /**
     * Constructor for Notification class.
     * Initializes a new Timer object for tracking the current time.
     */
    public Notification()
    {
        currentTime = new Timer();
    }


    /**
     * Starts the timer and checks for any scheduled events that are due to start in 10 minutes.
     * Sends a notification 10 minutes before the scheduled start of each event.
     *
     * @param m The model object containing the schedule and time blocks.
     */
    private void startTime(Model m)
    {
        int curTime = currentTime.getCurrentTimeInSecond();
        for (ScheduledTimeBlock s : m.getSchedule().getCurrDay().getAllTimeBlocks())
        {
            if(curTime == s.getStartTime() - 600)
            {
                sendNotification(s.gettagName(), "Be ready for your project.", "start_noti.jpeg", 1000);
            }
        }
    }

    /**
     * Checks for any scheduled events that are due to end in 10 minutes.
     * Sends a notification 10 minutes before the scheduled end of each event.
     *
     * @param m The model object containing the schedule and time blocks.
     */
    private void endTime(Model m)
    {
        int curTime = currentTime.getCurrentTimeInSecond();
        for (ScheduledTimeBlock s : m.getSchedule().getCurrDay().getAllTimeBlocks())
        {
            if(curTime == s.getendTime() - 600)
            {
                sendNotification(s.gettagName(), "Your project allocated time is going to finish.", "pause.png", 1000);
            }
        }
    }

    /**
     * Sends a summary notification at the end of the day.
     * Includes the total work time and the application used for the longest duration.
     *
     * @param m The model object containing the schedule, time blocks, and app tracker.
     */
    private void endOfTheDay(Model m)
    {
        Pair<String, Double> timer;
        HashMap<String, Double> get_total_time = new HashMap<>();
        int curTime = currentTime.getCurrentTimeInSecond();
        ScheduledTimeBlock last_time_block = m.getSchedule().getCurrDay().getlastTimeBlock();
        int time = 0;
        double total_time_by_App = 0;
        String app_name = "";
        if(last_time_block != null)
        {
            time = last_time_block.getendTime();
        }
        int total_time_worked = 0;
        int seconds ;
        int minutes ;
        int hours ;
        int app_seconds ;
        int app_minutes ;
        int app_hours ;

        if(curTime == time+10)
        {
            for(ScheduledTimeBlock s : m.getSchedule().getCurrDay().getAllTimeBlocks())
            {
                timer = s.getAppTracker().GetTopAppTime();
                if(get_total_time.containsKey(timer.getKey()))
                {
                    get_total_time.put(timer.getKey(), get_total_time.get(timer.getKey()) + timer.getValue());
                }
                else
                {
                    get_total_time.put(timer.getKey(), timer.getValue());
                }

                total_time_worked += s.getTotalWorkedTime();
            }


            for(String key : get_total_time.keySet())
            {
                if(total_time_by_App < get_total_time.get(key))
                {
                    total_time_by_App = get_total_time.get(key);
                    app_name = key;
                }
            }


            app_seconds = (int) (total_time_by_App % 60);
            app_minutes = (int) ((total_time_by_App / 60) % 60);
            app_hours = (int) ((total_time_by_App / 60) / 60);

            seconds = total_time_worked % 60;
            minutes = (total_time_worked / 60) % 60;
            hours = (total_time_worked / 60) / 60;

            sendNotification("End of the day", "You worked for " + hours + " hours " + minutes + " minutes " + seconds + " seconds " + "today." + "\n You spend most of the time on " + app_name + " almost " + app_hours + " hours " + app_minutes + " minutes " + app_seconds + " seconds " + "today.", "end_day.png", 1000);

        }
    }

    /**
     * Sends a notification with a specified title, message, image, and display duration.
     * If an image is provided, it is displayed in the notification.
     *
     * @param title   The title of the notification.
     * @param message The message content of the notification.
     * @param n       The filename of the image to be displayed in the notification.
     * @param second  The duration in seconds for which the notification should be displayed.
     */
    private void sendNotification(String title, String message, String n, int second)
    {
        ImageView imgView;

        if (n != null)
        {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(n)));
            imgView = new ImageView(img);
        }
        else
        {
            imgView = new ImageView();
        }
        imgView.setFitHeight(50);
        imgView.setFitWidth(50);

        Notifications noti = Notifications.create()
                .title(title)
                .text(message)
                .graphic(imgView)
                .hideAfter(Duration.seconds(second))
                .position(Pos.BOTTOM_RIGHT)
                .onAction( event -> {

                    // Slide in from the side
                    TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), (Node) event.getTarget());
                    slideIn.setFromX(300);
                    slideIn.setToX(0);

                    // Fade in effect
                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), (Node) event.getTarget());
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);

                    // Bounce effect at the end of the slide
                    TranslateTransition bounce = new TranslateTransition(Duration.seconds(0.2), (Node) event.getTarget());
                    bounce.setFromX(0);
                    bounce.setToX(20);
                    bounce.setCycleCount(2);
                    bounce.setAutoReverse(true);

                    // Play animations in sequence
                    new SequentialTransition(slideIn, fadeIn, bounce).play();
                });

        noti.darkStyle();
        noti.show();
    }

    /**
     * Displays notifications for start time, end time, and end of the day based on the provided model.
     *
     * @param m The model object containing the schedule and time blocks.
     */
    public void DisplayNotification(Model m)
    {
        startTime(m);
        endTime(m);
        endOfTheDay(m);
    }

    /**
     * Displays an alert message with a specified title and message.
     * Utilizes the 'sendNotification' method for displaying the alert.
     *
     * @param title   The title of the alert.
     * @param message The message content of the alert.
     */
    public void alertMessage(String title, String message)
    {
        sendNotification(title, message, null, 5);
    }

}

