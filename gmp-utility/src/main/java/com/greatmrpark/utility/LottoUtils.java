package com.greatmrpark.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LottoUtils {
    
    public String lottoNumbers() { 
        
        List<Integer> lottoNum = new ArrayList<Integer>();
         
        // List �ȿ� �ζǹ�ȣ �߰�
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
