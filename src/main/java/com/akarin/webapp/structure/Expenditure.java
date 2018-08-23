package com.akarin.webapp.structure;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class Expenditure {

    private final String name;
    private final double price;

    /**
     *
     * @param name
     * @param price
     */
    public Expenditure(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public static ArrayList<Expenditure> getArrayListFromJsonString(String jsonString) throws JsonParseException, JsonMappingException,
            IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<Expenditure> navigation = objectMapper.readValue(
                jsonString,
                objectMapper.getTypeFactory().constructCollectionType(
                        ArrayList.class, Expenditure.class));

        return navigation;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
