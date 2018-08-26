package com.akarin.webapp.structure;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class Expenditure {

    private final String spendingName;
    private final double spendingPrice;

    /**
     * @param spendingName
     * @param spendingPrice
     */
    public Expenditure(String spendingName, double spendingPrice) {
        this.spendingName = spendingName;
        this.spendingPrice = spendingPrice;
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

    public String getSpendingName() {
        return spendingName;
    }

    public double getSpendingPrice() {
        return spendingPrice;
    }
}
