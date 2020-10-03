package com.fara.inkamapp.Models.AirplainModels;

import android.util.Log;

import com.fara.inkamapp.Models.AirplainModels.AirplaneTicketList;
import com.fara.inkamapp.Models.AirplainModels.Response;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;

public class GetAirplaneTicketList  implements Serializable {
    public AirplaneTicketList data;
    public Response response;

    public GetAirplaneTicketList(SoapObject input) {

        try {
            data = new AirplaneTicketList((SoapObject) input.getProperty("data"));
            response = new Response((SoapObject) input.getProperty("response"));
        } catch (Exception e) {
            Log.i("Error Income", "GetAirplaneTicketList: "+e.toString());
        }
    }

    public GetAirplaneTicketList() {

    }
}
