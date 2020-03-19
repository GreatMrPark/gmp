package com.greatmrpark.utility;

import java.io.File;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;

public class TesseractUtils {

    public static void main(String[] args) { 
        String result = "";
        long totalTime = 0;
        long endTime = 0;
        long startTime = System.currentTimeMillis();
        File imageFile = new File("D:/project/download/images/1.PNG");
        Tesseract tesseract = new Tesseract(); 
        try { 

            tesseract.setDatapath("D:/project/Tesseract-OCR/tessdata");
            tesseract.setLanguage("kor");
            
            ImageIO.scanForPlugins();
            result = tesseract.doOCR(imageFile);

            System.out.println("==============================");
            System.out.println("Result For OCR : ");
            System.out.println("==============================");
            System.out.println(result);
            System.out.println("==============================");
            
            endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            
            System.out.println("Total Time Taken For OCR: " + (totalTime / 1000));
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = "";
        } 
    } 
}
