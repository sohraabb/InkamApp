package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import com.fara.inkamapp.Models.Error;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class Response  implements Serializable {
    public Boolean successful ;
    public Error[] errors ;
    public Response(SoapObject input) {

        try {
            successful = Boolean.valueOf( input.getPropertySafelyAsString("successful"));

            SoapObject dr= (SoapObject)input.getProperty("errors");

            for (int i = 0; i < dr.getPropertyCount(); i++) {
                errors[i]=( new Error((SoapObject) dr.getProperty(i)));

            }
        } catch (Exception e) {
            Log.i("Error Income", "GetAirplaneTicketList: "+e.toString());
        }
    }
}
