package com.example.saigeetha.homework9.stockdetails;

/**
 * Created by SaiGeetha on 5/5/2016.
 */
public class StockDetailsBean {


    String label;
    String value;

    public StockDetailsBean(){}

    public StockDetailsBean(String label, String value)
    {
        this.label=label;
        this.value=value;
    }

    public String getLabel() {
        return label;
    }


    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }




}
