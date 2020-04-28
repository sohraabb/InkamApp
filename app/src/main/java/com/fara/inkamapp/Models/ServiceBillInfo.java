package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class ServiceBillInfo {

    private ServiceBillDetails _serviceBillDetails;
    private Status _status;

    public ServiceBillDetails get_serviceBillDetails() {
        return _serviceBillDetails;
    }

    public void set_status(Status _status) {
        this._status = _status;
    }

    public Status get_status() {
        return _status;
    }

    public void set_serviceBillDetails(ServiceBillDetails _serviceBillDetails) {
        this._serviceBillDetails = _serviceBillDetails;
    }

    public ServiceBillInfo(SoapObject input) {

        try {

            SoapObject status = (SoapObject) input.getPropertySafely("Status");
            _status = new Status(status);

            SoapObject parameters = (SoapObject) input.getPropertySafely("Parameters");
            if (parameters != null)
                _serviceBillDetails = new ServiceBillDetails(parameters);

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }

}