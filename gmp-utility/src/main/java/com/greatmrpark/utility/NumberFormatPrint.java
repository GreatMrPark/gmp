package com.greatmrpark.utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 숫자표현
 * @author greatmrpark
 *
 */
public class NumberFormatPrint {

    public static void main(String[] args) {
        double dNumber = 10.254789;
        System.out.println("ori : " + dNumber);
        
        DecimalFormat df = new DecimalFormat(".#");
        System.out.println("DecimalFormat : " + df.format(dNumber));
        
        System.out.println("String.format : " + String.format("%.1f", dNumber));
        
        System.out.println("User Format : " + (int)(dNumber * 10) / 10.0);
        
        BigDecimal bd = new BigDecimal(String.valueOf(dNumber));
        System.out.println("BigDecimal : " + bd.setScale(1, BigDecimal.ROUND_DOWN));
    }
}
