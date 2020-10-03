package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AirItineraryPricingInfo  implements Serializable {
    public ItinTotalFare itinTotalFare ;
    public List<PtcFareBreakdowns> ptcFareBreakdowns ;
    public AirItineraryPricingInfo(SoapObject input) {

        try {
            itinTotalFare = new ItinTotalFare((SoapObject) input.getProperty("itinTotalFare"));

            SoapObject obj= ((SoapObject) input.getProperty("ptcFareBreakdowns"));
            ptcFareBreakdowns=new ArrayList<>();
            for (int i = 0; i < obj.getPropertyCount(); i++) {
              //  ptcFareBreakdowns.add( new PtcFareBreakdowns((SoapObject)((SoapObject) obj.getProperty(i))));

            }
        } catch (Exception e) {
            Log.i("Error Income", "AirItineraryPricingInfo: "+e.toString());
        }
    }
}
