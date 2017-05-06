package de.jverhoelen.util.converter;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StringArrayConverter implements AttributeConverter<String[], String> {

    @Override
    public String convertToDatabaseColumn(String[] strings) {
        return String.join(",", strings);
    }

    @Override
    public String[] convertToEntityAttribute(String s) {
        if (StringUtils.isEmpty(s)) {
            return new String[0];
        }

        return s.split(",");
    }
}
