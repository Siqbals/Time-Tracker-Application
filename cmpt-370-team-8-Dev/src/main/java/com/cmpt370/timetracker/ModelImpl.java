package com.cmpt370.timetracker;

public class ModelImpl implements Model {

    Schedule loadedSchedule;
    IdleDetector idleDetector;
    HotkeyHandler hotkeyHandler;

    HelloController hc;

    public ModelImpl(IdleDetector idleRef, HotkeyHandler hotkeyRef) {
        // TODO: Load with serialization
        this.loadedSchedule = new Schedule();
        this.idleDetector = idleRef;
        this.hotkeyHandler = hotkeyRef;
    }

    public void setLoadedSchedule(Schedule schedule) {
        this.loadedSchedule = schedule;
    }

    @Override
    public IdleDetector getIdleDetector() {
        return idleDetector;
    }

    @Override
    public HotkeyHandler getHotkeyHandler() {
        return hotkeyHandler;
    }

    @Override
    public Schedule getSchedule() {
        return loadedSchedule;
    }
}
