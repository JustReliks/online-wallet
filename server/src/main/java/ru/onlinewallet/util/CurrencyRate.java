package ru.onlinewallet.util;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class CurrencyRate {

    @JacksonXmlProperty(isAttribute = false,localName = "Cube")
    private String cube;

    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;

    @JacksonXmlProperty(isAttribute = true, localName = "rate")
    double rate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    // public getters and setters (omitted here for brevity)
}
