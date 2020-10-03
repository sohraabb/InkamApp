package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FlightSegments  implements Serializable {
    public AirortInfo departureAirport ;
    public AirortInfo arrivalAirport ;
    public String arrivalDate ;
    public String departureDate ;
    public AirlineInfo operatingAirLine ;
    public AirlineInfo marketingAirLine ;
    public BookingClassAvail bookingClassAvail ;
    public String flightNumber ;
    public String stopDuration ;
    public String flightDuration ;
    public List<Baggages> baggages ;

    public FlightSegments(SoapObject input) {

        try {
            flightNumber = input.getPropertyAsString("flightNumber");
            // stopDuration = input.getPropertyAsString("stopDuration");
            //flightDuration = input.getPropertyAsString("flightDuration");
            arrivalDate = input.getPropertyAsString("arrivalDate");
            departureDate = input.getPropertyAsString("departureDate");
            departureAirport = new AirortInfo((SoapObject) input.getProperty("departureAirport"));
            arrivalAirport = new AirortInfo((SoapObject) input.getProperty("arrivalAirport"));
            operatingAirLine = new AirlineInfo((SoapObject) input.getProperty("operatingAirLine"));

            marketingAirLine = new AirlineInfo((SoapObject) input.getProperty("marketingAirLine"));
            bookingClassAvail = new BookingClassAvail((SoapObject) input.getProperty("bookingClassAvail"));

            SoapObject lv= (SoapObject) input.getProperty("baggages");
            for (int i = 0; i < lv.getPropertyCount(); i++) {
                baggages.add( new Baggages((SoapObject) lv.getProperty(i)));

            }

        } catch (Exception e) {
            Log.i("Error Income", "FlightSegments: "+e.toString());
        }
    }
}
