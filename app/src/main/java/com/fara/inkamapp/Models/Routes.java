package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Routes {
    private String _to;
    private String _from;

    public void set_from(String _from) {
        this._from = _from;
    }

    public void set_to(String _to) {
        this._to = _to;
    }

    public String get_from() {
        return _from;
    }

    public String get_to() {
        return _to;
    }

    public Routes(SoapObject input) {
        try {

            _from = input.getPropertySafelyAsString("From");
            _to = input.getPropertySafelyAsString("To");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
