package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class BookingClassAvail  implements Serializable {
    public String resBookDesigCode;
    public String cabinType ;
    public BookingClassAvail(SoapObject input) {

        try {
            resBookDesigCode = input.getPropertyAsString("resBookDesigCode");
            cabinType = input.getPropertyAsString("cabinType");



        } catch (Exception e) {
            Log.i("Error Income", "BookingClassAvail: "+e.toString());
        }
    }
}
