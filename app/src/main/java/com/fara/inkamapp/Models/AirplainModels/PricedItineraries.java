package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PricedItineraries implements Serializable {
    public Boolean isDomestic ;
    public AirItinerary airItinerary ;
    public String moduleId;
    public String isRequiredPassportIssueDate ;
    public String provider ;
    public List<RefundConditions> refundConditions ;
    public AirItineraryPricingInfo airItineraryPricingInfo ;
    public PricedItineraries(SoapObject input) {

        try {
            airItineraryPricingInfo = new AirItineraryPricingInfo((SoapObject) input.getProperty("airItineraryPricingInfo"));
            airItinerary = new AirItinerary((SoapObject) input.getProperty("airItinerary"));
            isDomestic=Boolean.valueOf(input.getPropertySafelyAsString("isDomestic"));
            moduleId=input.getPropertySafelyAsString("moduleId");
            isRequiredPassportIssueDate=input.getPropertySafelyAsString("isRequiredPassportIssueDate");
            provider=input.getPropertySafelyAsString("provider");
            SoapObject obj= (SoapObject) input.getProperty("refundConditions");
            refundConditions= new ArrayList<>();
            for (int i = 0; i < obj.getPropertyCount(); i++) {
                refundConditions.add( new RefundConditions((SoapObject) obj.getProperty(i)));

            }
        } catch (Exception e) {
            Log.i("Error Income", "PricedItineraries: "+e.toString());
        }
    }
}
