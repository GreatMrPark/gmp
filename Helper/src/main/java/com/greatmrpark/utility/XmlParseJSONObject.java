package com.greatmrpark.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlParseJSONObject {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        XmlMapper xmlMapper = new XmlMapper();

        String schemaName = "D:/project/xml/mdms/COSEMpdu_GB8.xsd";
        //String xmlName = "D:/project/xml/mdms/meter.xml";
        //String xmlName = "D:/project/xml/mdms/LP.xml";
        //String xmlName = "D:/project/xml/mdms/최대 수요.xml";
        //String xmlName = "D:/project/xml/mdms/최대 수요_TOU적용.xml";
        String xmlName = "D:/project/xml/mdms/검침_TOU적용.xml";
        
        InputStream inputStream = new FileInputStream(new File(xmlName));
        String xml = IOUtils.toString(inputStream);

//        System.out.println("xml : " + xml);
          
        JSONObject jObject = XML.toJSONObject(xml);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object object = mapper.readValue(jObject.toString(), Object.class);

//        System.out.println("object : " + object);
          
        String json = mapper.writeValueAsString(object);
        System.out.println("json : " + json);

        Map entries  = mapper.readValue(json, LinkedHashMap.class);
//        System.out.println("entries : " + entries);
    }
}
