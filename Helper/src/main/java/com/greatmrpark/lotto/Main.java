package com.greatmrpark.lotto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Main {
    
    /**
     * 로또숫자
     */
    int num[] = {
               1,  2,  3,  4,  5
            ,  6,  7,  8,  9, 10
            , 11, 12, 13, 14, 15
            , 16, 17, 18, 19, 20
            , 21, 22, 23, 24, 25
            , 26, 27, 28, 29, 30
            , 31, 32, 33, 34, 35
            , 36, 37, 38, 39, 40
            , 41, 42, 43, 44, 45
    };
    
    /**
     * 당첨번호
     *  0 : time     : 회차
     *  1 : dt       : 추첨일
     *  2 : num1st   : 당첨번호 1번째
     *  3 : num2nd   : 당첨번호 2번째
     *  4 : num3th   : 당첨번호 3번째
     *  5 : num4th   : 당첨번호 4번째
     *  6 : num5th   : 당첨번호 5번째
     *  7 : num6th   : 당첨번호 6번째
     *  8 : numbns   : 당첨번호 보너스 번호
     *  9 : numsum   : 당첨번호 합
     * 10 : num1bndl : 1연속된번호묶음
     * 11 : num2bndl : 2번호연속묶음
     * 12 : num3bndl : 3번호연속묶음
     * 13 : num4bndl : 4번호연속묶음
     * 14 : num5bndl : 5번호연속묶음
     * 15 : num6bndl : 6번호연속묶음
     * {
     *    "time":""
     *  , "dt":""
     *  , "num1st":""
     *  , "num2nd":""
     *  , "num3th":""
     *  , "num4th":""
     *  , "num5th":""
     *  , "num6th":""
     *  , "numbns":""
     *  , "numsum":""
     *  , "numbndl":[
     *            {
     *                bndl : 1
     *              , srl : [
     *                        {1}
     *                      , {2}
     *                      , {3}
     *                  ]
     *            }
     *          , {}
     *          , {}
     *          , {}
     *          , {}
     *          , {}
     *      ]
     * }
     */
    HashMap<String, Object> numBndlSrlMap = new HashMap<>();
    HashMap<String, Object> numBndlMap = new HashMap<>();
    ArrayList<HashMap<String, Object>> wNumList = new ArrayList<>();
    
    /**
     * 로또번호
     */
    int num1st = 0; // 첫번째
    int num2nd = 0; // 두번째
    int num3th = 0; // 세번째
    int num4th = 0; // 네번째
    int num5th = 0; // 다섯번째
    int num6th = 0; // 여섯번째
    
    /**
     * 예상로또번호
     */
    int pnum1st = 0; // 첫번째  
    int pnum2nd = 0; // 두번째
    int pnum3th = 0; // 세번째
    int pnum4th = 0; // 네번째
    int pnum5th = 0; // 다섯번째
    int pnum6th = 0; // 여섯번째
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        for(int i = 1; i<45; i++) {
            System.out.println(String.format("%s, %s", "KOREA", "JAPAN"));
        }
    }
}
