package com.fara.inkamapp.Models.AirplainModels;

import java.io.Serializable;

public class TicketRequest  implements Serializable {
    public OriginDestinationInformation[] OriginDestinationInformation ;
    public AirTravelerAvails TravelerInfoSummary ;
    public Travelpreferences Travelpreferences ;

    /// 1=رفت
    /// 2=رفت و برگشت
    public String FlightType ;

}

