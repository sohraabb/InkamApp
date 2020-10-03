package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class PassengerTypeQuantity  implements Serializable {
    public int quantity ;
    public String passengerType ;
    public PassengerTypeQuantity(SoapObject input) {

        try {
            quantity =Integer.valueOf( input.getPropertyAsString("quantity"));
            passengerType =  input.getPropertyAsString("passengerType");


        } catch (Exception e) {
            Log.i("Error Income", "PassengerTypeQuantity: "+e.toString());
        }
    }
}
