package com.cmpt370.timetracker;

import java.util.Comparator;

 class timeBlockComparator implements Comparator<ScheduledTimeBlock> {
     @Override
     public int compare(ScheduledTimeBlock o1, ScheduledTimeBlock o2) {
         int comparedNum = 0;
         if(o1.getStartTime()<o2.getStartTime()){
             comparedNum =  -1;
         }

         else if (o1.getStartTime()>o2.getStartTime()) {
             comparedNum =  1;
         }

         return comparedNum;
     }

     @Override
     public boolean equals(Object obj) {
         return false;
     }
 }
