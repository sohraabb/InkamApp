package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AirplaneTicketList implements Serializable {
    public List<PricedItineraries> pricedItineraries ;
    public int count ;
    public String token ;
    public Boolean isComplete ;
    public AirplaneTicketList(SoapObject input) {

        try {
            count =Integer.valueOf( input.getPropertySafelyAsString("count"));
            token =  input.getPropertySafelyAsString("token");
            isComplete =Boolean.valueOf(  input.getPropertySafelyAsString("isComplete"));
            SoapObject dr= (SoapObject)input.getProperty("pricedItineraries");
            pricedItineraries= new ArrayList<>();
            for (int i = 0; i < dr.getPropertyCount(); i++) {
                pricedItineraries.add( new PricedItineraries((SoapObject) dr.getProperty(i)));

            }
        } catch (Exception e) {
            Log.i("Error Income", "AirplaneTicketList: "+e.toString());
        }
    }
}
