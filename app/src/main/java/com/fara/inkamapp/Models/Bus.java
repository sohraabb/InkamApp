package com.fara.inkamapp.Models;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class Bus extends BusSummary {
    private String CommissionPercentage;
    private List<BusSeate> Seates;
    public Bus(SoapObject input) {
        super(input);
        Seates= new ArrayList<>();
        CommissionPercentage = input.getPropertySafelyAsString("CommissionPercentage");
        SoapObject dr= (SoapObject)input.getProperty("Seates");

        for (int i = 0; i < dr.getPropertyCount(); i++) {
            Seates.add( new BusSeate((SoapObject) dr.getProperty(i)));

        }
    }

    public String getCommissionPercentage() {
        return CommissionPercentage;
    }

    public void setCommissionPercentage(String commissionPercentage) {
        CommissionPercentage = commissionPercentage;
    }

    public List<BusSeate> getSeates() {
        return Seates;
    }

    public void setSeates(List<BusSeate> seates) {
        Seates = seates;
    }
}
