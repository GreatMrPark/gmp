package com.greatmrpark.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLValidator {

    public static final String SCHEMA_FILE = "D:\\project\\xml\\mdms\\COSEMpdu_GB8.xsd";
    public static final String XML_FILE = "D:\\project\\xml\\mdms\\electric\\LP.xml";

    public static void main(String[] args) {
        XMLValidator XMLValidator = new XMLValidator();
        boolean valid = XMLValidator.validate(XML_FILE, SCHEMA_FILE);

        System.out.printf("%s validation = %b.", XML_FILE, valid);
    }

    private boolean validate(String xmlFile, String schemaFile) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(new File(schemaFile));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFile)));
            return true;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getResource(String filename) throws FileNotFoundException {
        URL resource = getClass().getClassLoader().getResource(filename);
        Objects.requireNonNull(resource);

        return resource.getFile();
    }
}
