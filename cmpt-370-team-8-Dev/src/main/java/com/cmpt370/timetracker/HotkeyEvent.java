package com.cmpt370.timetracker;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * The different kinds of actions a hotkey can represent, or be "bound" to.
 */
enum HotkeyAction {
    // Represents a hotkey that intends to start the timer, if it is not already running.
    START_TIMER,
    // Represents a hotkey that intends to stop the timer, if it is not already stopped.
    STOP_TIMER,
}

public class HotkeyEvent extends Event {
    public static final EventType<HotkeyEvent> HOTKEY_EVENT = new EventType(Event.ANY, "HotkeyEvent");
    HotkeyAction action;

    public HotkeyEvent(HotkeyAction action) {
        super(HOTKEY_EVENT);

        this.action = action;
    }

    public HotkeyAction getAction() {
        return action;
    }
}
