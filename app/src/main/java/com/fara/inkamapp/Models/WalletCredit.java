package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class WalletCredit {
    private String _status;
    private String _message;
    private Error _error;
    private Data _data;

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_data(Data _data) {
        this._data = _data;
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

    public Data get_data() {
        return _data;
    }

    public Error get_error() {
        return _error;
    }

    public WalletCredit(SoapObject input) {
        try {
            _status = input.getPropertySafelyAsString("Status");
            _message = input.getPropertySafelyAsString("Message");

            SoapObject data = (SoapObject) input.getPropertySafely("Data");
            _data = new Data(data);

            SoapObject error = (SoapObject) input.getPropertySafely("Error");
            _error = new Error(error);

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
