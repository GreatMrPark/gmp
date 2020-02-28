package com.greatmrpark.helper.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greatmrpark.helper.crawler.Go1372Crawler;

import net.sourceforge.tess4j.Tesseract;

public class CrawlerUtil {

    private Gson gson = new GsonBuilder().create();
    
    @Value("${gmp.file.images.download}")
    private static int imageDownloaPath;
    
    @Value("${gmp.ocrdatapath}")
    private static String datapath;
    
    @Autowired
    Go1372Crawler go1372Crawler;
        
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
    public static void downloadImage(String imgUrl) {
        
        System.out.println("IMG URL : " + imgUrl);
        
        try {
            URL url;
            url = new URL(imgUrl);
            String fileName = imgUrl.substring( imgUrl.lastIndexOf('/')+1, imgUrl.length() ); // 이미지 파일명 추출
            String ext = imgUrl.substring( imgUrl.lastIndexOf('.')+1, imgUrl.length() );  // 이미지 확장자 추출
            BufferedImage img = ImageIO.read(url);
            
            String fileFullPath = imageDownloaPath+fileName;
            
            ImageIO.write(img, ext, new File(fileFullPath));
            
            doOCR(fileFullPath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * OCR IMG TO TEXT
     * @param fileFullPath
     * @return
     */
    public static String doOCR(String fileFullPath) {
        
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
