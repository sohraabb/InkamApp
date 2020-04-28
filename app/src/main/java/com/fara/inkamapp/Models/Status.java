package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Status {
    private String _code;
    private String _description;

    public void set_code(String _code) {
        this._code = _code;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_code() {
        return _code;
    }

    public String get_description() {
        return _description;
    }

    public Status(SoapObject input) {
        try {
            _code = input.getPropertySafelyAsString("Code");
            _description = input.getPropertySafelyAsString("Description");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
