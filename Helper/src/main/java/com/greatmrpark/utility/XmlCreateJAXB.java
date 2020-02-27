package com.greatmrpark.utility;

import com.fasterxml.jackson.annotation.JsonValue;

public class XmlCreateJAXB {
    public static interface JAXBElementMixin {
        @JsonValue
        Object getValue();
    }
    
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
