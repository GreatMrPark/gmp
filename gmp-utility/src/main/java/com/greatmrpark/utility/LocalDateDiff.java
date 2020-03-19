package com.greatmrpark.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class LocalDateDiff {

    public static void main(String[] args) {

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                
        // month();
        // week();
        // day();
        hour();
        
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
    
    public static void month() {

        System.out.println("월월월월월월월월월월월월월월월월월월월월월월월월월월월월월월");
        LocalDateTime endDateMonthTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime startDateMonthTime = endDateMonthTime.minusMonths(6);

        System.out.println("startDateMonthTime : " + startDateMonthTime);
        System.out.println("endDateMonthTime : " + endDateMonthTime);

        long monthsBetween = ChronoUnit.MONTHS.between(startDateMonthTime, endDateMonthTime);
        System.out.println("monthsBetween : " + monthsBetween);
        for(int i =0; i <= monthsBetween; i++) {
            LocalDateTime regDate = startDateMonthTime.plusMonths(i);
            System.out.println(" regDate : " + regDate);
        }
        
    }
    
    public static void week() {

        System.out.println("주주주주주주주주주주주주주주주주주주주주주주주주주주주주주주");
        LocalDateTime endDateWeekTime = LocalDate.now().atStartOfDay();
        LocalDateTime startDateWeekTime = endDateWeekTime.minusMonths(6).with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        System.out.println("startDateWeekTime : " + startDateWeekTime);
        System.out.println("endDateWeekTime : " + endDateWeekTime);
        long weeksBetween = ChronoUnit.WEEKS.between(startDateWeekTime, endDateWeekTime);
        System.out.println("weeksBetween : " + weeksBetween);
        for(int i =0; i <= weeksBetween; i++) {
            LocalDateTime regDate = startDateWeekTime.plusWeeks(i);
            System.out.println(" regDate : " + regDate);
        }

    }
    
    public static void day() {

        System.out.println("일일일일일일일일일일일일일일일일일일일일일일일일일일일일일일");
        LocalDateTime endDateDayTime = LocalDate.now().atStartOfDay();
        LocalDateTime startDateDayTime = endDateDayTime.minusMonths(6);

        System.out.println("startDateDayTime : " + startDateDayTime);
        System.out.println("endDateDayTime : " + endDateDayTime);
        long daysBetween = ChronoUnit.DAYS.between(startDateDayTime, endDateDayTime);
        System.out.println("daysBetween : " + daysBetween);
        for(int i =0; i <= daysBetween; i++) {
            LocalDateTime regDate = startDateDayTime.plusDays(i);
            System.out.println(" regDate : " + regDate);
        }
    }
    
    public static void hour() {

        System.out.println("시시시시시시시시시시시시시시시시시시시시시시시시시시시시시시");
        LocalDateTime endDateHourTime = LocalDate.now().atStartOfDay();
        LocalDateTime startDateHourTime = endDateHourTime.minusMonths(6);

        System.out.println("startDateHourTime : " + startDateHourTime);
        System.out.println("endDateHourTime : " + endDateHourTime);
        long hoursBetween = ChronoUnit.HOURS.between(startDateHourTime, endDateHourTime);
        System.out.println("hoursBetween : " + hoursBetween);
        for(int i =0; i <= hoursBetween; i++) {
            LocalDateTime regDate = startDateHourTime.plusHours(i);
            System.out.println(" regDate : " + regDate);
        }
    }

}
