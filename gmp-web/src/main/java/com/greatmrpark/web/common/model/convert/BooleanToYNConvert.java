package com.greatmrpark.web.common.model.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

@Convert
public class BooleanToYNConvert implements AttributeConverter<Boolean, String> {
    public String convertToDatabaseColumn(Boolean value) {
        return (value == null || !value) ? "N" : "Y";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equals(value);
    }
}
