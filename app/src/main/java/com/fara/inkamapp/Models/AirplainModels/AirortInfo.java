package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class AirortInfo  implements Serializable {
    public String code ;
    public String name ;
    public String terminal ;
    public String cityName ;
    public AirortInfo(SoapObject input) {

        try {
            code = input.getPropertyAsString("code");
            name = input.getPropertyAsString("name");
            terminal = input.getPropertyAsString("terminal");
            cityName = input.getPropertyAsString("cityName");


        } catch (Exception e) {
            Log.i("Error Income", "AirortInfo: "+e.toString());
        }
    }
}
