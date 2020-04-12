package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class ProductAndService {

    private String _id;
    private String _resourceStringId;
    private String _picURL;
    private String _name;
    private int _point;
    private String _accountId;

    public void set_point(int _point) {
        this._point = _point;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_accountId(String _accountId) {
        this._accountId = _accountId;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_picURL(String _picURL) {
        this._picURL = _picURL;
    }

    public void set_resourceStringId(String _resourceStringId) {
        this._resourceStringId = _resourceStringId;
    }

    public String get_id() {
        return _id;
    }

    public int get_point() {
        return _point;
    }

    public String get_accountId() {
        return _accountId;
    }

    public String get_name() {
        return _name;
    }

    public String get_picURL() {
        return _picURL;
    }

    public String get_resourceStringId() {
        return _resourceStringId;
    }

    public ProductAndService(SoapObject input) {

        try {
            _id = input.getPropertySafelyAsString("ID");
            _resourceStringId = input.getPropertySafelyAsString("ResourceStringID");
            _picURL = input.getPropertySafelyAsString("PicURL");
            _name = input.getPropertySafelyAsString("Name");
            _point = Integer.parseInt(input.getPropertySafelyAsString("point"));
            _accountId = input.getPropertySafelyAsString("AccountID");


        } catch (Exception e) {
            Log.e("ProductAndServicde Soap", e.toString());
        }
    }
}

