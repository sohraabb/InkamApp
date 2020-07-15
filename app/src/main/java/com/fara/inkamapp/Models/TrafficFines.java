package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class TrafficFines {
    private CarFinesParameters _carFinesParameters;
    private Status _status;

    public void set_status(Status _status) {
        this._status = _status;
    }

    public CarFinesParameters get_carFinesParameters() {
        return _carFinesParameters;
    }

    public void set_carFinesParameters(CarFinesParameters _carFinesParameters) {
        this._carFinesParameters = _carFinesParameters;
    }

    public Status get_status() {
        return _status;
    }

    public TrafficFines(SoapObject input) {
        try {

            SoapObject status = (SoapObject) input.getPropertySafely("Status");
            _status = new Status(status);

            Object parametersObj = input.getPropertySafely("Parameters");
            if (parametersObj.toString() != null) {
                SoapObject parameters = (SoapObject) parametersObj;
                _carFinesParameters = new CarFinesParameters(parameters);
            }

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
