package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class BusCities {
    private String _id;
    private String _name;
    private String _englishName;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_englishName(String _englishName) {
        this._englishName = _englishName;
    }

    public String get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_englishName() {
        return _englishName;
    }

    public BusCities(SoapObject input){
        try {
            _id = input.getPropertySafelyAsString("ID");
            _name = input.getPropertySafelyAsString("Name");
            _englishName = input.getPropertySafelyAsString("EnglishName");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
