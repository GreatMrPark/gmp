package com.greatmrpark.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LottoUtils {

    public static void main(String[] args) {
        System.out.println("start lotto numbers");
        List<String> lottoNums = new ArrayList<>();

        // List numbers
        for (int i = 1; i <= 45; i++) {
            lottoNums.add(String.valueOf(i));
        }
        
        List<String> result = new ArrayList<>();
        reculsion(lottoNums, result, 0, lottoNums.size(), 6);
    }
    
    /**
     * 모든로또번호추출(조합구하기)
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 11. 16. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method reculsion
     *
     * @param lottoNums
     * @param result
     * @param index
     * @param n
     * @param r
     */
    public static void reculsion(List<String> lottoNums, List<String> result, int index, int n, int r) {
        
        if (r == 0) {
            System.out.println(result.toString());
            return;
        }
        
        for(int i = index; i < n; i++) {
            result.add(lottoNums.get(i));
            reculsion(lottoNums, result, i + 1, n, r - 1);
            result.remove(result.size() - 1);
        }
    }
        
    public String lottoNumbers() { 
        
        List<Integer> lottoNum = new ArrayList<Integer>();
         
        // List numbers
        for (int i = 1; i <= 45; i++) {
            lottoNum.add(i);
        }
 
        // 번호를 섞는다
        Collections.shuffle(lottoNum);
 
        int[] lottoNums = new int[6];
        for (int i = 0; i < 6; i++) {
            lottoNums[i] = lottoNum.get(i);
        }
         
        // 순번대로 정렬
        Arrays.sort(lottoNums);
 
        return Arrays.toString(lottoNums);
    }
}
