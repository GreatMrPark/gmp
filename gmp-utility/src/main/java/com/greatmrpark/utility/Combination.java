package com.greatmrpark.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;

/**
 * 로또번호생성
 * @author greatmrpark 
 *
 */
public class Combination {
    
    private static ArrayList<Object> list = new ArrayList<Object>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String line = System.getProperty("line.separator");
    
    public boolean isLogWriter = false;
    public int c = 0;
    public static void main(String[] args) {
        
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

        Combination lottoNumGen = new Combination();
        
        int n = num.length;
        int r = 6;
        int[] combArr = new int[n];

        lottoNumGen.doCombination(combArr, n, r, 0, 0, num);
        
        System.out.println("list.size : " + list.size());
        
        /**
         * 파일생성
         */
        System.out.println("==============================");
        System.out.println("파일을 생성 시작.");
        System.out.println("==============================");
        Gson gson = new Gson();
        gson.toJson(list);
        String message = gson.toString();
        
        File file = new File("D:/project/workspace/GmpLottoHelper/lottori.txt");
        FileWriter writer = null;
        
        try {
            // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
            writer = new FileWriter(file, false);
            writer.write(message);
            writer.flush();
            
            System.out.println("DONE");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("==============================");
        System.out.println("파일을 생성 완료.");
        System.out.println("==============================");
    }

    public void doCombination(int[] combArr, int n, int r, int index, int target, int[] arr){
        
        if(r == 0){

            if (c > 0) {
                if (isLogWriter) System.out.println(",{");
            } else {
                if (isLogWriter) System.out.println(" {");
            }
            

            HashMap<String, Object> mapKey = new HashMap<String, Object>();
            Gson gson = new Gson();
            c++;

            int no = 0;
            int num1st = 0; // 첫번째        0
            int num2nd = 0; // 두번째        1
            int num3th = 0; // 세번째        2
            int num4th = 0; // 네번째        3
            int num5th = 0; // 다섯번째     4
            int num6th = 0; // 여섯번째     5
            int numsum = 0; // 합               6
            int nummul = 1; // 곱               7
            int numbndl1st = 0; // 묶음        8
            int numbndl2nd = 0; // 묶음        9
            int numbndl3th = 0; // 묶음       10
            int numbndl4th = 0; // 묶음       11
            int numbndl5th = 0; // 묶음       12
            int numbndl6th = 0; // 묶음       13
                    
            int combTemp[] = new int[15];
            
            for(int i = 0; i < index; i++) {
                
                combTemp[i] = arr[combArr[i]];
                numsum += arr[combArr[i]];
                nummul *= arr[combArr[i]];
                no++;
            }

            combTemp[no++] = numsum;
            combTemp[no++] = nummul;
            
            // 연번체크
            SequnceNumbers sequnceNumbers = new SequnceNumbers();
            
            int[] numbndl = sequnceNumbers.checkSequnceNumbers(combTemp,6);

            numbndl1st = numbndl[1]; // 묶음        8
            numbndl2nd = numbndl[2]; // 묶음        9
            numbndl3th = numbndl[3]; // 묶음       10
            numbndl4th = numbndl[4]; // 묶음       11
            numbndl5th = numbndl[5]; // 묶음       12
            numbndl6th = numbndl[6]; // 묶음       13
            
            if(c==1) {

                System.out.println("num1st: " + combTemp[0]); // 첫번째        0
                System.out.println("num2nd: " + combTemp[1]); // 두번째        1
                System.out.println("num3th: " + combTemp[2]); // 세번째        2
                System.out.println("num4th: " + combTemp[3]); // 네번째        3
                System.out.println("num5th: " + combTemp[4]); // 다섯번째     4
                System.out.println("num6th: " + combTemp[5]); // 여섯번째     5
                
                System.out.println("numbndl1st: " + numbndl1st);
                System.out.println("numbndl2nd: " + numbndl2nd);
                System.out.println("numbndl3th: " + numbndl3th);
                System.out.println("numbndl4th: " + numbndl4th);
                System.out.println("numbndl5th: " + numbndl5th);
                System.out.println("numbndl6th: " + numbndl6th);
            }
            
            combTemp[no++] = numbndl1st; // 1자리 연속된번호묶음(8)
            combTemp[no++] = numbndl2nd; // 2자리 연속된번호묶음(9)
            combTemp[no++] = numbndl3th; // 3자리 연속된번호묶음(10)
            combTemp[no++] = numbndl4th; // 4자리 연속된번호묶음(11)
            combTemp[no++] = numbndl5th; // 5자리 연속된번호묶음(12)
            combTemp[no++] = numbndl6th; // 6자리 연속된번호묶음(13)

            if(c % 100000 == 0) {
                if (isLogWriter) System.out.println("[" + sdf.format(new Date()) + "][" + c + "][" + list.size() + "] 생성되었습니다.");
            } 
            
            if (isLogWriter) System.out.println("\"" + c + "\" : ");
            if (isLogWriter) System.out.println("\t{");

            HashMap<String, Integer> mapNum = new HashMap<String, Integer>();
            for(int k = 0; k < combTemp.length; k++) {

                if(k ==  0) {
                    if (isLogWriter) System.out.println("\t\t \"num1st\" : \"" + combTemp[k] + "\"");
                    mapNum.put("num1st", combTemp[k]);
                }
                if(k ==  1) {
                    if (isLogWriter) System.out.println("\t\t,\"num2nd\" : \"" + combTemp[k] + "\"");
                    mapNum.put("num2nd", combTemp[k]);
                }
                if(k ==  2) {
                    if (isLogWriter) System.out.println("\t\t,\"num3th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("num3th", combTemp[k]);
                }
                if(k ==  3) {
                    if (isLogWriter) System.out.println("\t\t,\"num4th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("num4th", combTemp[k]);
                }
                if(k ==  4) {
                    if (isLogWriter) System.out.println("\t\t,\"num5th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("num5th", combTemp[k]);
                }
                if(k ==  5) {
                    if (isLogWriter) System.out.println("\t\t,\"num6th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("num6th", combTemp[k]);
                }
                if(k ==  6) {
                    if (isLogWriter) System.out.println("\t\t,\"numsum\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numsum", combTemp[k]);
                }
                if(k ==  7) {
                    if (isLogWriter) System.out.println("\t\t,\"nummul\" : \"" + combTemp[k] + "\"");
                    mapNum.put("nummul", combTemp[k]);
                }
                if(k ==  8) {
                    if (isLogWriter) System.out.println("\t\t,\"numbndl1st\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numbndl1st", combTemp[k]);
                }
                if(k ==  9) {
                    if (isLogWriter) System.out.println("\t\t,\"numbndl2nd\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numbndl2nd", combTemp[k]);
                }
                if(k == 10) {
                    if (isLogWriter) System.out.println("\t\t,\"numbndl3th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numbndl3th", combTemp[k]);
                }
                if(k == 11) {
                    if (isLogWriter) System.out.println("\t\t,\"numbndl4th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numbndl4th", combTemp[k]);
                }
                if(k == 12) {
                    if (isLogWriter) System.out.println("\t\t,\"numbndl5th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numbndl5th", combTemp[k]);
                }
                if(k == 13) {
                    if (isLogWriter) System.out.println("\t\t,\"numbndl6th\" : \"" + combTemp[k] + "\"");
                    mapNum.put("numbndl6th", combTemp[k]);
                }
            }
            
            if (isLogWriter) System.out.println("\t}");
            
            if(mapNum != null) {
                String key = String.valueOf(c);
                mapKey.put(key,mapNum);
            }
            
            if (mapKey != null) {
                // list.add(mapKey);

                File file = new File("D:/project/workspace/GmpLottoHelper/lottori.txt");
                FileWriter writer = null;
                
                try {
                    // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
                    writer = new FileWriter(file, true);
                    writer.write(gson.toJson(mapKey)+line);
                    writer.flush();
                    
                    //System.out.println("DONE");
                } catch(IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(writer != null) writer.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        } else if(target == n) {
            return;
        } else{
            combArr[index] = target;
            doCombination(combArr, n, r-1, index+1, target+1, arr); // (i)
            doCombination(combArr, n, r, index, target+1, arr); //(ii)
        }

        if (isLogWriter) System.out.println("}");
    }
}
