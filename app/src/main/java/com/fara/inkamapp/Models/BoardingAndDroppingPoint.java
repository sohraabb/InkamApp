package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class BoardingAndDroppingPoint {
    private String _city;
    private String _terminal;

    public void set_city(String _city) {
        this._city = _city;
    }

    public void set_terminal(String _terminal) {
        this._terminal = _terminal;
    }

    public String get_city() {
        return _city;
    }

    public String get_terminal() {
        return _terminal;
    }

    public BoardingAndDroppingPoint(SoapObject input){
        try {
            _city = input.getPropertySafelyAsString("City");
            _terminal = input.getPropertySafelyAsString("Terminal");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
