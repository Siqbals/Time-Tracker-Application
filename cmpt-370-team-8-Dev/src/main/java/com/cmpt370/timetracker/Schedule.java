package com.cmpt370.timetracker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;

public class Schedule implements Serializable {

    //list of days in the schedule
    private List<Day> days;

    /**
     * Constructor
     */
    Schedule(){
        this.days = new ArrayList<>();
    }

    public List<Day> getAllDays() {
        return days;
    }

    /**
     * To get the current day
     * @return current day
     */
    public Day getCurrDay(){
        Day fetchedDay = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar obj = Calendar.getInstance();
        String currDate = formatter.format(obj.getTime());
        int currDay = Integer.parseInt(currDate.substring(0,2));
        int currMonth = Integer.parseInt(currDate.substring(3,5));
        int currYear = Integer.parseInt(currDate.substring(6,10));

        for( int i=0; i< days.size(); i++){
            if (    days.get(i).getDay() == currDay &&
                    days.get(i).getMonth() == currMonth &&
                    days.get(i).getYear() == currYear  ){

                fetchedDay =  days.get(i);
            }
        }

        if (fetchedDay == null){
            fetchedDay = new Day(currDay,currMonth,currYear);
            days.add(fetchedDay);
        }

        return fetchedDay;
    }

    /**
     * To get a specified day
     * @param day
     * @param month
     * @param year
     * @return day
     */
    public Day getDay(int day, int month, int year){

        Day fetchDay = null;

        for( int i=0; i< days.size(); i++){
            if (    days.get(i).getDay() == day &&
                    days.get(i).getMonth() == month &&
                    days.get(i).getYear() == year  ){

                fetchDay =  days.get(i);
            }
        }

        if (fetchDay == null){
            fetchDay = new Day(day,month,year);
            days.add(fetchDay);
        }
        return fetchDay;
    }


}
