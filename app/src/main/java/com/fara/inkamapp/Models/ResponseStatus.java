package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class ResponseStatus {

    private String _id;
    private String _message;
    private String _status;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_id() {
        return _id;
    }

    public String get_message() {
        return _message;
    }

    public String get_status() {
        return _status;
    }

    public ResponseStatus(SoapObject input) {

        try {
            _id = input.getPropertySafelyAsString("ID");
            _message = input.getPropertySafelyAsString("Message");
            _status = input.getPropertySafelyAsString("Status");

        } catch (Exception e) {
            Log.e("ResponseStatus Soap", e.toString());
        }
    }
}
