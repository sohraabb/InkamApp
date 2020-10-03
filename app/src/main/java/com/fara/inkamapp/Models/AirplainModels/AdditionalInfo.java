package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import com.fara.inkamapp.Models.BusCities;

import org.ksoap2.serialization.SoapObject;

public class AdditionalInfo  {
    private BusCities City;
    private String Name ;
    private String EnglishName ;
    public AdditionalInfo(SoapObject input){
        try {
            EnglishName = input.getPropertySafelyAsString("EnglishName");
            Name = input.getPropertySafelyAsString("Name");
            City = new BusCities((SoapObject) input.getProperty("City"));

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
    public BusCities getCity() {
        return City;
    }

    public void setCity(BusCities city) {
        City = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String englishName) {
        EnglishName = englishName;
    }
}
