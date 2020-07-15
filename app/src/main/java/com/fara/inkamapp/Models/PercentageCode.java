package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class PercentageCode {
    private String _id;
    private String _userId;
    private boolean _isDefault;
    private String _userCount;
    private String _name;
    private String _code;

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_code(String _code) {
        this._code = _code;
    }

    public void set_isDefault(boolean _isDefault) {
        this._isDefault = _isDefault;
    }

    public void set_userCount(String _userCount) {
        this._userCount = _userCount;
    }

    public String get_userId() {
        return _userId;
    }

    public String get_name() {
        return _name;
    }

    public String get_id() {
        return _id;
    }

    public String get_code() {
        return _code;
    }

    public boolean is_isDefault() {
        return _isDefault;
    }

    public String get_userCount() {
        return _userCount;
    }
    public PercentageCode(SoapObject input){
        try {
            _id = input.getPropertySafelyAsString("ID");
            _userId = input.getPropertySafelyAsString("UserID");
            _isDefault = Boolean.parseBoolean(input.getPropertySafelyAsString("IsDefault"));
            _userCount = input.getPropertySafelyAsString("UserCount");
            _name = input.getPropertySafelyAsString("Name");
            _code = input.getPropertySafelyAsString("Code");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
