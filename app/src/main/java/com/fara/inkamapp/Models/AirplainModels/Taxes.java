package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class Taxes  implements Serializable {
    public String taxCode;
    public String taxName;
    public String amount;
    public String currencyCode;
    public String decimalPlaces;

    public Taxes(SoapObject input) {

        try {

            taxCode = input.getPropertyAsString("taxCode");
            taxName = input.getPropertyAsString("taxName");
            amount = input.getPropertyAsString("amount");
            currencyCode = input.getPropertyAsString("currencyCode");
            decimalPlaces = input.getPropertyAsString("decimalPlaces");


        } catch (Exception e) {
            Log.i("Error Income", "Taxes: " + e.toString());
        }
    }
}
