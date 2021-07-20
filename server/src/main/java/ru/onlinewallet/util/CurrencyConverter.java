package ru.onlinewallet.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

//@JacksonXmlRootElement(localName = "Cube")
//public class CurrencyConverter {
//    @JacksonXmlProperty(isAttribute = false, localName = "Cube")
//    @JacksonXmlElementWrapper(useWrapping = false)
//    private List<CurrencyRate> rates;
//
//    public List<CurrencyRate> getRates() {
//        return rates;
//    }
//
//    // public getters and setters (omitted here for brevity)
//}

@JacksonXmlRootElement(localName = "Envelope")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyConverter {

    @JacksonXmlProperty(isAttribute = false, localName = "Cube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CurrencyRate> rates;

    public List<CurrencyRate> getRates() {
        return rates;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "time")
    private String _time;


    public String get_time() {
        return _time;
    }

    // Setter Methods

    public void set_time(String _time) {
        this._time = _time;
    }
}