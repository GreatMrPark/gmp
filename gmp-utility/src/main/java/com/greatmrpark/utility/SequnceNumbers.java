package com.greatmrpark.utility;

/**
 * 연속된 숫자 묶음 개수
 * @author greatmrpark
 *
 */
public class SequnceNumbers {
    

    public static void main(String[] args){ 

        int target[] = {
                   1
                   ,  2,  3
                ,  4 
                , 5, 6
            };
        int counterLength = target.length;
        
        SequnceNumbers ex = new SequnceNumbers();
        
        int[] resultSequnceNumbers = ex.checkSequnceNumbers(target,counterLength);

        if(resultSequnceNumbers != null && resultSequnceNumbers.length > 0) {
            int index = 0;
            for(int number: resultSequnceNumbers) {
                System.out.println(index++ + " : " + number);
            }
        }
        
    }
    
    /**
     * n 과 n+1… n+n-1 을 비교해서 카운트한다.
     * @param target
     * @param counterLength
     * @return
     */
    public int[] checkSequnceNumbers(int[] target, int counterLength) {
        
        int[] sequentialCounter = new int[counterLength+1];
         
        int count = 1;
        int len = target.length;
        
        for (int i = 0 ; i < len; i++) {
            int subCount = 0;
            
            for (int j = 1; j < len; j++) {
                if (target[j] == target[i]+1) {
                    subCount = subCount + 1;
                } else {
                    continue;
                }
                
            }
            
            count = count + subCount;
            if (subCount == 0) {
                sequentialCounter[count] = sequentialCounter[count] + 1;
                count = 1;
            }
        }
        if(target[0]==1 && target[1]==2 && target[2]==3 && target[3]==4 && target[4]==5 && target[5]==6) {
            System.out.println("r " + target[0] + " : " + sequentialCounter[1]);
            System.out.println("r " + target[1] + " : " + sequentialCounter[2]);
            System.out.println("r " + target[2] + " : " + sequentialCounter[3]);
            System.out.println("r " + target[3] + " : " + sequentialCounter[4]);
            System.out.println("r " + target[4] + " : " + sequentialCounter[5]);
            System.out.println("r " + target[5] + " : " + sequentialCounter[6]);
        }
        return sequentialCounter;
    }
}
