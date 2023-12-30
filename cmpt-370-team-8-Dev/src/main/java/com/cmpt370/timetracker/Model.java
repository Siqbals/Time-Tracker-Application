package com.cmpt370.timetracker;

public interface Model {
    public IdleDetector getIdleDetector();
    public HotkeyHandler getHotkeyHandler();
    public Schedule getSchedule();
    public void setLoadedSchedule(Schedule schedule);
}
