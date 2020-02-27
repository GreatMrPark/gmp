package com.greatmrpark.utility;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class LocalDateWeekOfMonth {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
        int weekNumber = date.get(woy);

        System.out.println("date : " + date);
        System.out.println("weekNumber : " + weekNumber);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        
        

    }

}
