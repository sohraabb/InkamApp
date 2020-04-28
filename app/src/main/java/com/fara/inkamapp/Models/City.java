package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class City {
    private String _id;
    private String _provinceID;
    private String _name;
    private String _resourceID;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_provinceID(String _provinceID) {
        this._provinceID = _provinceID;
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

    public String get_provinceID() {
        return _provinceID;
    }

    public String get_resourceID() {
        return _resourceID;
    }

    public City(SoapObject input) {

        try {
            _id = input.getPropertySafelyAsString("ID");
            _provinceID = input.getPropertySafelyAsString("ProvinceID");
            _name = input.getPropertySafelyAsString("Name");
            _resourceID = input.getPropertySafelyAsString("ResourceID");

        } catch (Exception e) {
            Log.e("ResponseStatus Soap", e.toString());
        }
    }
}
