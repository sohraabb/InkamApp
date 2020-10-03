package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class BoardingPoint {
    private String City;
    private String Terminal;
    private AdditionalInfo AdditionalInfo;

    public BoardingPoint(SoapObject input) {
        try {
            City = input.getPropertySafelyAsString("City");
            Terminal = input.getPropertySafelyAsString("Terminal");
            AdditionalInfo = new AdditionalInfo((SoapObject) input.getProperty("AdditionalInfo"));

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getTerminal() {
        return Terminal;
    }

    public void setTerminal(String terminal) {
        Terminal = terminal;
    }

    public AdditionalInfo getAdditionalInfo() {
        return AdditionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        AdditionalInfo = additionalInfo;
    }
}
