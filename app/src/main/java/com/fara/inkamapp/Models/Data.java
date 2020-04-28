package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Data {
    private String _token;
    private String _expireDate;
    private String _status;
    private String _message;

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void set_expireDate(String _expireDate) {
        this._expireDate = _expireDate;
    }

    public String get_message() {
        return _message;
    }

    public String get_status() {
        return _status;
    }

    public String get_token() {
        return _token;
    }

    public String get_expireDate() {
        return _expireDate;
    }

    public Data(SoapObject input){
        try {
            _token = input.getPropertySafelyAsString("Token");
            _expireDate = input.getPropertySafelyAsString("ExpireDate");
            _status = input.getPropertySafelyAsString("Status");
            _message = input.getPropertySafelyAsString("Message");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
