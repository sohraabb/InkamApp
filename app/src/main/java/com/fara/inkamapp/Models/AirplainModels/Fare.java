package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class Fare  implements Serializable {
    public String amount ;
    public String currencyCode ;
    public String decimalPlaces ;
    public Fare(SoapObject input) {

        try {
            amount =  input.getPropertyAsString("amount");
            currencyCode =  input.getPropertyAsString("currencyCode");
            decimalPlaces =  input.getPropertyAsString("decimalPlaces");

        } catch (Exception e) {
            Log.i("Error Income", "Fare: "+e.toString());
        }
    }
}
