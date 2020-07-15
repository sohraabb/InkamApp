package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class WalletCredit {
    private String _status;
    private String _message;

    private Error _error;
    private Data Data;

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void setData(Data data) {
        this.Data = data;
    }

    public void set_error(Error _error) {
        this._error = _error;
    }

    public String get_status() {
        return _status;
    }

    public String get_message() {
        return _message;
    }

    public Data getData() {
        return Data;
    }

    public Error get_error() {
        return _error;
    }

    public WalletCredit(SoapObject input) {
        try {
            _status = input.getPropertySafelyAsString("Status");
            _message = input.getPropertySafelyAsString("Message");

            SoapObject data = (SoapObject) input.getPropertySafely("Data");
            Data = new Data(data, "", _status, _message);

            SoapObject error = (SoapObject) input.getPropertySafely("Error");
            _error = new Error(error);

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
