package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class AirItinerary {
    public List<LeaveOptions> leaveOptions ;
   // public List<Object> returnOptions ;
    public String airItineraryRph ;
    public AirItinerary(){}
    public AirItinerary(SoapObject input) {

        try {
            airItineraryRph = input.getPropertyAsString("airItineraryRph");
            SoapObject lv= (SoapObject) input.getProperty("leaveOptions");
            leaveOptions= new ArrayList<>();
            for (int i = 0; i < lv.getPropertyCount(); i++) {
                leaveOptions.add( new LeaveOptions((SoapObject) lv.getProperty(i)));

            }
            SoapObject obj= (SoapObject) input.getProperty("returnOptions");
          //  returnOptions=new ArrayList<>();
            for (int i = 0; i < obj.getPropertyCount(); i++) {
              //  returnOptions.add( new Object((SoapObject) obj.getProperty(i)));

            }
        } catch (Exception e) {
            Log.i("Error Income", "AirItinerary: "+e.toString());
        }
    }
}
