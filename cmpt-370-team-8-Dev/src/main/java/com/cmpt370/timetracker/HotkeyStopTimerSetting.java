package com.cmpt370.timetracker;

import javafx.scene.input.KeyCode;

public class HotkeyStopTimerSetting extends HotkeySetting {
    public static final HotkeyAction action = HotkeyAction.STOP_TIMER;

    public HotkeyStopTimerSetting(HotkeyHandler hotkeyHandlerRef, KeyCode code) {
        super(hotkeyHandlerRef, code);
    }

    @Override
    public String getName() {
        return "Stop Timer";
    }

    @Override
    public HotkeyAction getAction() {
        return this.action;
    }
}
