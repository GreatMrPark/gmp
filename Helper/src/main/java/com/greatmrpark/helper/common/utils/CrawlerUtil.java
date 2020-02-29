package com.greatmrpark.helper.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;

@Slf4j
public class CrawlerUtil {
        
    /**
     * 휴식
     * @param ptime
     */
    public static void sleep(int ptime) {
        ptime = 1000 * 1;
        try {
            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            log.debug("{} 초 휴식", ptime / 1000);
            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Thread.sleep(ptime);
        } catch (InterruptedException e) {e.printStackTrace();}
        
    }
        
    /**
     * 숫자인지 확인
     * @param s
     * @return
     */
    public static boolean isStringDouble(String s) {
        
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 이미지 다운로드
     * @param imgUrl
     */
    public static String downloadImage(String filePath, String imgUrl) {
        String fileName = "";
        String fileFullPath = "";
        try {
            URL url = new URL(imgUrl);
            URLConnection con = url.openConnection();
            HttpURLConnection exitCode = (HttpURLConnection)con;
            
            if (exitCode.getResponseCode() == 200) {
                fileName = imgUrl.substring( imgUrl.lastIndexOf('/')+1, imgUrl.length() ); // 이미지 파일명 추출
                String ext = imgUrl.substring( imgUrl.lastIndexOf('.')+1, imgUrl.length() );  // 이미지 확장자 추출
                BufferedImage img = ImageIO.read(url);                
                fileFullPath = filePath + "/" + fileName;
                ImageIO.write(img, ext, new File(fileFullPath));
            }
            
        } catch (Exception e) {
            log.error("downloadImage : {}", e.getMessage());
        }
        
        return fileFullPath;
    }
    
    /**
     * OCR IMG TO TEXT
     * @param fileFullPath
     * @return
     */
    public static String doOCR(String fileFullPath, String datapath) {
        
        System.out.println("fileFullPath : " + fileFullPath);
        
        String result = "";
        long totalTime = 0;
        long endTime = 0;
        long startTime = System.currentTimeMillis();
        File file = new File(fileFullPath);
        Tesseract tesseract = new Tesseract(); 
        try { 

            tesseract.setDatapath(datapath); // ocr 경로
            tesseract.setLanguage("kor");
            
            ImageIO.scanForPlugins();
            result = tesseract.doOCR(file);

            System.out.println("==============================");
            System.out.println("Result For OCR : ");
            System.out.println("==============================");
            System.out.println(result);
            System.out.println("==============================");
            
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            
            System.out.println("Total Time Taken For OCR: " + (totalTime / 1000));
            
            return result;
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = "";
            return result;
        } 
    }
}
