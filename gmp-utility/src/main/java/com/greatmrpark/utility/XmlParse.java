package com.greatmrpark.utility;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParse {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        String schemaName = "D:/project/xml/mdms/COSEMpdu_GB8.xsd";
        String xmlName = "D:/project/xml/mdms/검침_TOU적용.xml";
        Schema schema = loadSchema(schemaName);
        Document document = parseXmlDom(xmlName);
        
        validateXml(schema, document);
        
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

//            System.out.println("node : " + node.getNodeName());
//            System.out.println("node : " + node.getNodeValue());
            
            NodeList childs = node.getChildNodes();
            for(int j = 0; j < childs.getLength(); j++) {
                Node child = childs.item(j);
                
//                System.out.println("name : " + child.getNodeName());
                
                NodeList childNodes = child.getChildNodes();
                for(int k = 0; k < childNodes.getLength(); k++) {
                    Node childNode = childNodes.item(k);

//                    System.out.println("childNode name : " + childNode.getNodeName());
                    NodeList childNodeChilds = childNode.getChildNodes();

                    System.out.println(childNode.getNodeName() + " nodes count : " + childNodeChilds.getLength());
                    
                    if (childNode.getNodeName().contains("x:value-list")) {
                        System.out.println("@@@@@@@@@@@@@@@@@@@@");
                        for(int z = 0; z < childNodeChilds.getLength(); z++) {

                            Node childNodeChild = childNodeChilds.item(z);

                            if (childNodeChild.getNodeName().contains("x:Data")) {
                                System.out.println("--------------------");
                                System.out.println("index : " + z);
                                
                                System.out.println("name : " + childNodeChild.getNodeName());
                                System.out.println("text : " + childNodeChild.getTextContent().trim());
                            }
                        }
                        System.out.println("@@@@@@@@@@@@@@@@@@@@");
                    }
                }
            }
        }
    }
    
    public static void validateXml(Schema schema, Document document) throws Exception {
        
         /**
          *  3. Get a validator from the schema.
          */
         Validator validator = schema.newValidator();
         System.out.println("Validator Class: " + validator.getClass().getName());
       
         /**
          *  validating the document against the schema
          */
         validator.validate(new DOMSource(document));
       }
       
       public static Schema loadSchema(String schemaFileName) throws Exception {
           Schema schema = null;
           
           /**
            * 1. Lookup a factory for the W3C XML Schema language
            */
           String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
           SchemaFactory factory = SchemaFactory.newInstance(language);
           
           /**
            * 2. Compile the schema. Here the schema is loaded from a java.io.File, but
            * you could use a java.net.URL or a javax.xml.transform.Source instead.
            */
           schema = factory.newSchema(new File(schemaFileName));
           return schema;
       }
       
       public static Document parseXmlDom(String xmlName) throws Exception {
           Document document = null;
           
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setNamespaceAware(true);
           DocumentBuilder builder = factory.newDocumentBuilder();
           document = builder.parse(new File(xmlName));
           
           return document;
       }
}
