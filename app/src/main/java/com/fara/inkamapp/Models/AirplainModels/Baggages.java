package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class Baggages  implements Serializable {
    public String ageType ;
    public int allowanceAmount ;
    public String unit ;
    public int pieceAmount ;
    public String baggageTextDisplay ;
    public Baggages(SoapObject input) {

        try {
            ageType = input.getPropertyAsString("ageType");
            unit = input.getPropertyAsString("unit");
            baggageTextDisplay = input.getPropertyAsString("baggageTextDisplay");
            allowanceAmount =Integer.valueOf( input.getPropertyAsString("allowanceAmount"));
            pieceAmount =Integer.valueOf( input.getPropertyAsString("pieceAmount"));


        } catch (Exception e) {
            Log.i("Error Income", "Baggages: "+e.toString());
        }
    }
}
