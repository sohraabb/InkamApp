package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class RefundConditions  implements Serializable {
    public String titleKey ;
    public TitleValues titleValues ;
    public String value ;
    public String priority ;
    public RefundConditions(SoapObject input) {

        try {
            titleKey = input.getPropertyAsString("titleKey");
            value = input.getPropertyAsString("value");
            priority = input.getPropertyAsString("priority");
            titleValues = new TitleValues((SoapObject) input.getProperty("titleValues"));


        } catch (Exception e) {
            Log.i("Error Income", "RefundConditions: "+e.toString());
        }
    }
}
