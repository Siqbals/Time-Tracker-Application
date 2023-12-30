package com.cmpt370.timetracker;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class Day implements Serializable {

    private List<ScheduledTimeBlock> timeBlocks;
    private int year;
    private int month;
    private int day;


    /**
     * Constructor
     * @param year
     * @param month
     * @param day
     */
    public Day(int day, int month, int year){
        this.timeBlocks = new ArrayList<>();
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public List<ScheduledTimeBlock> getAllTimeBlocks(){
        return timeBlocks;
    }

    /**
     *Gets the current time block
     * @return Timeblock
     */
    public ScheduledTimeBlock getCurrentTimeBlock() {
        ScheduledTimeBlock TB = null;
        for (int i = 0; i < timeBlocks.size(); i++) {
            if (timeBlocks.get(i).getStartTime() <= timeBlocks.get(i).getCurrentTimeInSecond() && timeBlocks.get(i).getendTime() >= timeBlocks.get(i).getCurrentTimeInSecond()) {
                TB = timeBlocks.get(i);
            }
        }
        return TB;
    }

    /**
     *Fetches a timeblock by a specified tag
     * @param Tag
     * @return Timeblock
     */
     public ScheduledTimeBlock getBlockByTag(String Tag){
        ScheduledTimeBlock TB = null;
        for( int i=0; i< timeBlocks.size(); i++){
            if (timeBlocks.get(i).gettagName() == Tag){
                 TB =  timeBlocks.get(i);
            }
        }
         return TB;
     }

    /**
     *Removes a Timeblock by a specified tag
     * @param Tag
     */
     public void removeTimeBlock(String Tag){
        ScheduledTimeBlock TB = getBlockByTag(Tag);
        timeBlocks.remove(TB);
     }

    /**
     *Creates a new instance of a Timeblock
     * @param startTime
     * @param endTime
     * @param Tag
     * @return new Timeblock
     */
    public ScheduledTimeBlock newTimeBlock(int startTime, int endTime, String Tag) {
        ScheduledTimeBlock nTB = new ScheduledTimeBlock();
        nTB.setStartTime(startTime);
        nTB.setEndTime(endTime);
        nTB.setTagName(Tag);

        if (!hasOverlap(nTB)) {
            timeBlocks.add(nTB);
            Collections.sort(timeBlocks, new timeBlockComparator());
            return nTB;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input, TimeBlock already exists. Please choose a different TimeBlock.");
            alert.showAndWait();
            return null;
        }

    }

    /**
     * Checks for overlaps within timeblocks- assistant function for creating a new timeblock
     * @param newBlock
     * @return boolean
     */
    private boolean hasOverlap(ScheduledTimeBlock newBlock) {
        for (ScheduledTimeBlock existingBlock : timeBlocks) {
            if (newBlock.getStartTime() < existingBlock.getendTime() && newBlock.getendTime() > existingBlock.getStartTime()) {
                return true; // Overlapping time block found
            }
        }
        return false; // No overlapping time blocks found
    }

    /**
     * Fetched a timeblock by its start time
     * @param startTime
     * @return Timeblock
     */
     public ScheduledTimeBlock getBlockByStartTime(int startTime){
         ScheduledTimeBlock TB = null;
         for( int i=0; i< timeBlocks.size(); i++){
             if (timeBlocks.get(i).getStartTime() == startTime){
                 TB =  timeBlocks.get(i);
             }
         }
         return TB;

     }

    /**
     * Fetches a Timeblock by end time
     * @param endTime
     * @return Timeblock
     */
     public ScheduledTimeBlock getBlockByEndTime(int endTime){
         ScheduledTimeBlock TB = null;
         for( int i=0; i< timeBlocks.size(); i++){
             if (timeBlocks.get(i).getendTime() == endTime){
                 TB =  timeBlocks.get(i);
             }
         }
         return TB;

     }

    /**
     *Fetches the last timeblock of the day
     * @return Timeblock
     */
    public ScheduledTimeBlock getlastTimeBlock()
    {
        ScheduledTimeBlock TB;
        if(!timeBlocks.isEmpty()) {
            TB = timeBlocks.get(0);
            for (int i = 0; i < timeBlocks.size(); i++) {
                if (timeBlocks.get(i).getendTime() > TB.getendTime()) {
                    TB = timeBlocks.get(i);
                }
            }
        }
        else
        {
            TB = null;
        }

        return TB;
    }
}
