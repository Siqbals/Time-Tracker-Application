package com.cmpt370.timetracker;

import javafx.scene.input.KeyCode;

public class HotkeyStartTimerSetting extends HotkeySetting {
    public static final HotkeyAction action = HotkeyAction.START_TIMER;

    public HotkeyStartTimerSetting(HotkeyHandler hotkeyHandlerRef, KeyCode code) {
        super(hotkeyHandlerRef, code);
    }

    @Override
    public String getName() {
        return "Start Timer";
    }

    @Override
    public HotkeyAction getAction() {
        return this.action;
    }
}
