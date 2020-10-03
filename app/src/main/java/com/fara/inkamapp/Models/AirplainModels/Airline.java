package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Airline  implements Serializable {
   private   String airlineId ;
    private  String title ;
    private  String countryTitle ;
    private String countryISOCode ;
    private String iataCode ;
    private  String persianTitle ;
    private String icaoCode ;

    public Airline() {
    }
    public Airline(SoapObject input) {

        try {
            airlineId = input.getPropertyAsString("airlineId");
            title = input.getPropertyAsString("title");
            countryTitle = input.getPropertyAsString("countryTitle");
            countryISOCode = input.getPropertyAsString("countryISOCode");
            iataCode = input.getPropertyAsString("iataCode");
            persianTitle = input.getPropertyAsString("persianTitle");
            icaoCode = input.getPropertyAsString("icaoCode");



        } catch (Exception e) {
            Log.i("Error Income", "Airline: "+e.toString());
        }
    }
    public String getIcaoCode() {
        return icaoCode;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public String getPersianTitle() {
        return persianTitle;
    }

    public void setPersianTitle(String persianTitle) {
        this.persianTitle = persianTitle;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getCountryISOCode() {
        return countryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        this.countryISOCode = countryISOCode;
    }

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(String airlineId) {
        this.airlineId = airlineId;
    }
}
