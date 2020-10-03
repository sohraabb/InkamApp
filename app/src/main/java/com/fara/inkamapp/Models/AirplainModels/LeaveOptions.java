package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeaveOptions  implements Serializable {
    public List<FlightSegments> flightSegments ;
    public String providerMetaData ;
    public Boolean isChartered ;
    public Boolean isRefundable ;
    public int resBookDesigQuantity ;
    public String totalStopDuration ;
    public String totalFlightDuration ;
    public Boolean hasStop ;
    public String flightAgencyTitle;

    public LeaveOptions(SoapObject input) {

        try {
            providerMetaData = input.getPropertyAsString("providerMetaData");

            isChartered =Boolean.valueOf( input.getPropertyAsString("isChartered"));
            isRefundable =Boolean.valueOf( input.getPropertyAsString("isRefundable"));
            hasStop =Boolean.valueOf( input.getPropertyAsString("hasStop"));
            resBookDesigQuantity =Integer.valueOf( input.getPropertyAsString("resBookDesigQuantity"));

            SoapObject obj= (SoapObject) input.getProperty("flightSegments");
            flightSegments= new ArrayList<>();
            for (int i = 0; i < obj.getPropertyCount(); i++) {
                flightSegments.add( new FlightSegments((SoapObject) obj.getProperty(i)));

            }
            totalStopDuration = input.getPropertyAsString("totalStopDuration");
            totalFlightDuration = input.getPropertyAsString("totalFlightDuration");
            flightAgencyTitle = input.getPropertyAsString("flightAgencyTitle");
        } catch (Exception e) {
            Log.i("Error Income", "AirItinerary: "+e.toString());
        }
    }
}
