package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class PhoneBillInfo {

    private PhoneBillParameters _phoneBillParameters;
    private Status _status;

    public void set_status(Status _status) {
        this._status = _status;
    }

    public void set_phoneBillParameters(PhoneBillParameters _phoneBillParameters) {
        this._phoneBillParameters = _phoneBillParameters;
    }

    public Status get_status() {
        return _status;
    }

    public PhoneBillParameters get_phoneBillParameters() {
        return _phoneBillParameters;
    }

    public PhoneBillInfo(SoapObject input) {

        try {

            SoapObject status = (SoapObject) input.getPropertySafely("Status");
            _status = new Status(status);

            SoapObject parameters = (SoapObject) input.getPropertySafely("Parameters");
            if (parameters != null)
                _phoneBillParameters = new PhoneBillParameters(parameters);


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }


}
