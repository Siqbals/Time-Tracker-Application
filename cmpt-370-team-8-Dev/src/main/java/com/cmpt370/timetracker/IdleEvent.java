package com.cmpt370.timetracker;

import javafx.event.Event;
import javafx.event.EventType;

enum IdleMessage {
    BECAME_IDLE,
    RETURNED_FROM_IDLE
}

public class IdleEvent extends Event {
    public static final EventType<IdleEvent> IDLE_EVENT = new EventType(Event.ANY, "IdleEvent");

    private IdleMessage message;
    private double idleTime;

    public IdleEvent(IdleMessage message, double idleTime) {
        super(IDLE_EVENT);
        this.message = message;
        this.idleTime = idleTime;
    }

    /**
     * Dictates what subtype of IdleEvent this event is
     * There can be two types of Idle Events:
     * The first is an event signalling the user has just gone idle
     * The second is an event signalling the user has just returned to their device
     * and is no longer idle.
     */
    public IdleMessage getMessage() {
        return message;
    }

    /**
     * Gets how long, in seconds, since the user became idle.
     *
     * More precisely, this returns the time since the last input event was received
     * by the Operating System.
     */
    public double getIdleTime() {
        return idleTime;
    }
}
