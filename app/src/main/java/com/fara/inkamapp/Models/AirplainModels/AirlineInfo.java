package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class AirlineInfo  implements Serializable {
    public String airlineId ;
    public String code ;
    public String name ;
    public String countryCode ;
    public AirlineInfo(SoapObject input) {

        try {
            code = input.getPropertyAsString("code");
            name = input.getPropertyAsString("name");
            airlineId = input.getPropertyAsString("airlineId");
            countryCode = input.getPropertyAsString("countryCode");


        } catch (Exception e) {
            Log.i("Error Income", "AirlineInfo: "+e.toString());
        }
    }
}
