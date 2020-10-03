package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.List;

public class PassengerFares  implements Serializable {
    public Fare baseFare ;
    public List<Taxes> taxes ;
    public Fare commsionValue ;
    public Fare originalFare ;
    public Fare totalFare ;
    public PassengerTypeQuantity passengerTypeQuantity ;
    public PassengerFares(SoapObject input) {
        input=(SoapObject)input.getProperty(0);
        try {
            baseFare = new Fare((SoapObject) input.getProperty("baseFare"));
            totalFare = new Fare((SoapObject) input.getProperty("totalFare"));
            commsionValue = new Fare((SoapObject) input.getProperty("commsionValue"));
            originalFare = new Fare((SoapObject) input.getProperty("originalFare"));
            passengerTypeQuantity = new PassengerTypeQuantity((SoapObject) input.getProperty("passengerTypeQuantity"));
            SoapObject obj= (SoapObject)((SoapObject) input.getProperty("taxes")).getProperty(0);
            for (int i = 0; i < obj.getPropertyCount(); i++) {
                taxes.add( new Taxes((SoapObject) obj.getProperty(i)));

            }

        } catch (Exception e) {
            Log.i("Error Income", "PassengerFares: "+e.toString());
        }
    }
}
