package com.greatmrpark.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.text.StrSubstitutor;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;

@Slf4j
public class CrawlerUtil {
        
    /**
     * 휴식
     * @param ptime
     */
    public static void sleep(double ptime) {
        try {
            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            log.debug("{} 초 휴식", ptime / 1000);
            log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Thread.sleep((long) ptime);
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
                fileFullPath = filePath + fileName;
                ImageIO.write(img, ext, new File(fileFullPath));
            }
            
        } catch (Exception e) {
            log.error("downloadImage : {}", e.getMessage());
        }
        
        return fileFullPath;
    }

    public static String download(String filePath, String path) {
        String fileName = "";
        String fileFullPath = "";
        try {
            URL url = new URL(path);
            URLConnection con = url.openConnection();
            HttpURLConnection exitCode = (HttpURLConnection)con;
            OutputStream outStream = null;
            InputStream is = null;
            int byteWritten = 0;
            if (exitCode.getResponseCode() == 200) {
                fileName = path.substring( path.lastIndexOf('/')+1, path.length() ); 
                String ext = path.substring( path.lastIndexOf('.')+1, path.length() );  
                InputStream in = new URL(path).openStream();
                
                fileFullPath = filePath + fileName;
                Files.copy(in, Paths.get(fileFullPath));
                                
            }
            
        } catch (Exception e) {
            log.error("download : {}", e.getMessage());
        }
        
        return fileFullPath;
    }
    
    /**
     * OCR IMG TO TEXT
     * @param fileFullPath
     * @return
     */
    public static String doOCR(String fileFullPath, String datapath) {
        
        String result = "";
        long totalTime = 0;
        long endTime = 0;
        long startTime = System.currentTimeMillis();
        File file = new File(fileFullPath);
        Tesseract tesseract = new Tesseract(); // Tesseract tesseract = Tesseract.getInstance();
        try { 

            tesseract.setDatapath(datapath); // ocr 경로
            tesseract.setLanguage("kor");
            
            ImageIO.scanForPlugins();
            result = tesseract.doOCR(file);
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            
            log.debug("Total Time Taken For OCR: {} Seconds" , (totalTime / 1000));
        } catch (Exception e) {
            log.error("doOCR : {} ", "");
            result = "";
        } 

        return result;
    }
    
    /**
     * 메시지 템플릿
     *
     * @history
     * ------------------------------------
     * 2020. 3. 3. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method messageTemplate
     *
     * @param message
     * @param value
     * @return
     */
    public static String messageTemplate(String templateString , Map<String, Object> substitutes) {
        StrSubstitutor sub = new StrSubstitutor(substitutes);
        return sub.replace(templateString);
    }
    
    /**
     * 이메일 검정
     *
     * @history
     * <pre>
     * ------------------------------------
     * 2020. 3. 4. greatmrpark 최초작성
     * ------------------------------------
     * </pre>
     *
     * @Method isValidEmail
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        boolean b = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email.replaceAll(" ", ""));
        if(m.matches()) {
            b = true; 
        }
        return b;
    }
}
