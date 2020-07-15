package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class CarFinesParameters {

    private ArrayList<TrafficFinesDetails> _trafficFinesDetails;
    private String _plateNumber;
    private long _totalAmount;

    public void set_trafficFinesDetails(ArrayList<TrafficFinesDetails> _trafficFinesDetails) {
        this._trafficFinesDetails = _trafficFinesDetails;
    }

    public void set_plateNumber(String _plateNumber) {
        this._plateNumber = _plateNumber;
    }

    public void set_totalAmount(long _totalAmount) {
        this._totalAmount = _totalAmount;
    }

    public long get_totalAmount() {
        return _totalAmount;
    }

    public String get_plateNumber() {
        return _plateNumber;
    }

    public ArrayList<TrafficFinesDetails> get_trafficFinesDetails() {
        return _trafficFinesDetails;
    }
    public CarFinesParameters(SoapObject input) {
        try {
            _trafficFinesDetails = new ArrayList<>();

            SoapObject trafficDetails = (SoapObject) input.getPropertySafely("Details");
            if (trafficDetails != null) {
                for (int i = 0; i < trafficDetails.getPropertyCount(); ++i) {

                    _trafficFinesDetails.add(new TrafficFinesDetails((SoapObject) trafficDetails.getProperty(i)));
                }
            }

            _plateNumber = input.getPropertySafelyAsString("PlateNumber");
            _totalAmount = Long.parseLong(input.getPropertySafelyAsString("TotalAmount"));

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
