package com.f5.resumerry.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {

    /**
     * From Boolean(true/false) value to Y/N value
     *
     * @param attribute
     * @return
     */
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "N" : "Y";
    }

    /**
     * From Y/N value to Boolean(true/false) value
     *
     * @param dbData
     * @return
     */
    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equalsIgnoreCase(dbData);
    }
}