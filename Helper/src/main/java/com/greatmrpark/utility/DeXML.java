package com.greatmrpark.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DeXML {

    public DeXML() {}

    public Map<String, Object> toMap(InputStream is) {
        return toMap(new InputSource(is));
    }

    public Map<String, Object> toMap(String xml) {
        return toMap(new InputSource(new StringReader(xml)));
    }

    private Map<String, Object> toMap(InputSource input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(input);
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            return visitChildNode(root);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }catch (SAXException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Check if node type is TEXT or CDATA and contains actual text (i.e. ignore
    // white space).
    private boolean isText(Node node) {
        return ((node.getNodeType() == Element.TEXT_NODE || node.getNodeType() == Element.CDATA_SECTION_NODE)
                && node.getNodeValue() != null && !node.getNodeValue().trim().isEmpty());
    }

    private Map<String, Object> visitChildNode(Node node) {    
        Map<String, Object> map = new HashMap<String, Object>();

        // Add the plain attributes to the map - fortunately, no duplicate attributes are allowed.
        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();
            for (int j = 0; j < nodeMap.getLength(); j++) {
                Node attribute = nodeMap.item(j);
                map.put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }

        NodeList nodeList = node.getChildNodes();

        // Any text children to add to the map?
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (isText(child)) {
                list.add(child.getNodeValue());
            }
        }
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                map.put(null, list);
            } else {
                map.put(null, list.get(0));
            }
        }

        // Process the element children.
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {

            // Ignore anything but element nodes.
            Node child = nodeList.item(i);
            if (child.getNodeType() != Element.ELEMENT_NODE) {
                continue;
            }

            // Get the subtree.
            Map<String, Object> childsMap = visitChildNode(child);

            // Now, check if this is key already exists in the map. If it does
            // and is not a List yet (if it is already a List, simply add the
            // new structure to it), create a new List, add it to the map and
            // put both elements in it. 
            if (map.containsKey(child.getNodeName())) {
                Object value = map.get(child.getNodeName());
                List<Object> multiple = null;
                if (value instanceof List) {
                    multiple = (List<Object>)value;
                } else {
                    map.put(child.getNodeName(), multiple = new ArrayList<Object>());
                    multiple.add(value);
                }
                multiple.add(childsMap);
            } else {
                map.put(child.getNodeName(), childsMap);
            }
        }
        return map;
    }     
}
