package com.greatmrpark.utility;

import java.util.Base64;
import java.util.Scanner;

/**
 * Base64Encode
 * @author greatmrpark
 *
 */
public class Base64Encode {

    public static void main(String[] args) {
        String temp; //3320851 , 0106021
        
        System.out.print("문자열을 입력 : ");
        Scanner scan = new Scanner(System.in);
        while (true) { 
        if(scan!=null) {
        
            temp = scan.nextLine();
            
            String encode = Base64.getEncoder().encodeToString(temp.getBytes());
            System.out.println("encode: "+ encode);
    
            String decode = new String(Base64.getDecoder().decode(encode));
            System.out.println("decode: "+ decode);
        }
        

        System.out.println("");
        System.out.print("문자열을 입력 : ");
        }
        
    }
}
