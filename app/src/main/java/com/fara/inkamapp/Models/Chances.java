package com.fara.inkamapp.Models;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chances {

    private String _id;
    private String _actionID;
    private Date _dateTime;
    private String _persianDateTime;
    private String _code;

    public void set_dateTime(Date _dateTime) {
        this._dateTime = _dateTime;
    }

    public void set_code(String _code) {
        this._code = _code;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_actionID(String _actionID) {
        this._actionID = _actionID;
    }

    public void set_persianDateTime(String _persianDateTime) {
        this._persianDateTime = _persianDateTime;
    }

    public Date get_dateTime() {
        return _dateTime;
    }

    public String get_code() {
        return _code;
    }

    public String get_id() {
        return _id;
    }

    public String get_actionID() {
        return _actionID;
    }

    public String get_persianDateTime() {
        return _persianDateTime;
    }

    public Chances(SoapObject input) {
        _id = input.getPropertySafelyAsString("ID");
        _actionID = input.getPropertySafelyAsString("ActionID");
        _persianDateTime = input.getPropertySafelyAsString("PersianDate");
        _code = input.getPropertySafelyAsString("Code");

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            _dateTime = formatter.parse(input.getPropertySafelyAsString("Datetime"));
        } catch (Exception ex) {
            _dateTime = new Date();
        }

    }
}