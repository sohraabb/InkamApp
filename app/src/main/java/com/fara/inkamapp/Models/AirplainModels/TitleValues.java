package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class TitleValues  implements Serializable {
    public String day ;
    public String hour ;
    public TitleValues(SoapObject input) {

        try {
            day = input.getPropertyAsString("day");
            hour = input.getPropertyAsString("hour");


        } catch (Exception e) {
            Log.i("Error Income", "TitleValues: "+e.toString());
        }
    }
}
