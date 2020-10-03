package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class ContactList {
    private String _id;
    private String _userId;
    private String _name;
    private String _phone;
    private String _description;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_description() {
        return _description;
    }

    public String get_userId() {
        return _userId;
    }

    public ContactList(SoapObject input) {
        try {
            _id = input.getPropertySafelyAsString("ID");
            _userId = input.getPropertySafelyAsString("UserID");
            _name = input.getPropertySafelyAsString("Name");
            _phone = input.getPropertySafelyAsString("Phone");
            _description = input.getPropertySafelyAsString("Description");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}