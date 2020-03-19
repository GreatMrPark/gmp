package com.greatmrpark.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlParseJackson {

    public static void main(String[] args) throws IOException {
        
        String schemaName = "D:/project/xml/mdms/COSEMpdu_GB8.xsd";
        String xmlName = "D:/project/xml/mdms/LP.xml";
        
        File file = new File(xmlName);
        
        String xml = inputStreamToString(new FileInputStream(file));
        
        System.out.println("xml : " + xml);

        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readValue(xml,JsonNode.class);
        System.out.println("node : " + node);        
    }
    
    public static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}
