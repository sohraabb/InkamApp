package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class ItinTotalFare  implements Serializable {
    public Fare baseFare;
    public Fare totalFare ;
    public Fare totalCommsionValue;
    public ItinTotalFare(SoapObject input) {

        try {
            baseFare = new Fare((SoapObject) input.getProperty("baseFare"));
            totalFare = new Fare((SoapObject) input.getProperty("totalFare"));
            totalCommsionValue = new Fare((SoapObject) input.getProperty("totalCommsionValue"));

        } catch (Exception e) {
            Log.i("Error Income", "ItinTotalFare: "+e.toString());
        }
    }
}
