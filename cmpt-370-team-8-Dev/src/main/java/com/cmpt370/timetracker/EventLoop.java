//package com.cmpt370.timetracker;
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.util.Duration;
//
//public class EventLoop {
//    private Timeline timeline;
//    protected Notification notification;
//
//    public EventLoop()
//    {
//        this.timeline = createTimeline();
//        this.notification = new Notification();
//        timeline.play();
//
//    }
//
//    private Timeline createTimeline()
//    {
//        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
//            this.notification.DisplayNotification();
//        });
//        Timeline timeline = new Timeline(keyFrame);
//        timeline.setCycleCount(Animation.INDEFINITE);
//        return timeline;
//    }
//}
//
