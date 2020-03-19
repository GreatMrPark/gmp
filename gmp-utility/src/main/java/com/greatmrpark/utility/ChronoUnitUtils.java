package com.greatmrpark.utility;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ChronoUnitUtils {

	public static void main(String[] args) {

        int missingDataCount = 0;
    	int period = 1;

		LocalDateTime startDateTime = LocalDateTime.of(2020, 2, 17, 9, 51, 0); // 2020-02-17 09:51:00
		LocalDateTime endDateTime   = LocalDateTime.of(2020, 2, 17, 9, 52, 0); // 2020-02-17 09:52:00

		long minutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime);

    	int s = (int) Math.floorDiv(minutes, period);

    	if(minutes > period) {
    		missingDataCount = s - 1; // 누럭 검심 데이터 간격수
    	}

        System.out.println("startDateTime : " + startDateTime);
        System.out.println("endDateTime : " + endDateTime);
        System.out.println("period : " + period);
        System.out.println("minutes : " + minutes);
        System.out.println("s : " + s);
        System.out.println("missingDataCount : " + missingDataCount);
	}
}
