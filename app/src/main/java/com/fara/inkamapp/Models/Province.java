package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Province {
    private String _id;
    private String _resourceID;
    private String _name;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_resourceID(String _resourceID) {
        this._resourceID = _resourceID;
    }

    public String get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_resourceID() {
        return _resourceID;
    }

    public Province(SoapObject input) {

        try {
            _id = input.getPropertySafelyAsString("ID");
            _resourceID = input.getPropertySafelyAsString("ResourceID");
            _name = input.getPropertySafelyAsString("Name");

        } catch (Exception e) {
            Log.e("ResponseStatus Soap", e.toString());
        }
    }
}
