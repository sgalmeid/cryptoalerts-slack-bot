package de.jverhoelen.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jverhoelen.currency.plot.Plot;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter(autoApply = true)
public class PlotConverter implements AttributeConverter<Plot, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Plot plot) {
        try {
            return objectMapper.writeValueAsString(plot);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @Override
    public Plot convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, Plot.class);
        } catch (IOException e) {
            return new Plot();
        }
    }
}
