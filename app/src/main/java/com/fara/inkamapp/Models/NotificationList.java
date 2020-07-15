package com.fara.inkamapp.Models;

import android.net.ParseException;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationList {
    private String _id;
    private String _userID;
    private String _title;
    private String _message;
    private boolean _status;
    private Date _dateTime;
    private String _persianDateTIme;


    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_status(boolean _status) {
        this._status = _status;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_dateTime(Date _dateTime) {
        this._dateTime = _dateTime;
    }

    public void set_persianDateTIme(String _persianDateTIme) {
        this._persianDateTIme = _persianDateTIme;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_userID(String _userID) {
        this._userID = _userID;
    }

    public String get_id() {
        return _id;
    }

    public String get_message() {
        return _message;
    }

    public Date get_dateTime() {
        return _dateTime;
    }

    public String get_persianDateTIme() {
        return _persianDateTIme;
    }

    public String get_title() {
        return _title;
    }

    public String get_userID() {
        return _userID;
    }

    public boolean is_status() {
        return _status;
    }

    public NotificationList(SoapObject input) {
        try {
            _id = input.getPropertySafelyAsString("ID");
            _userID = input.getPropertySafelyAsString("UserID");
            _title = input.getPropertySafelyAsString("Title");
            _message = input.getPropertySafelyAsString("Message");
            _status = Boolean.parseBoolean(input.getPropertySafelyAsString("Status"));

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                _dateTime = formatter.parse(input.getPropertySafelyAsString("Datetime"));
            } catch (Exception ex) {
                _dateTime = new Date();
            }

            _persianDateTIme = input.getPropertySafelyAsString("DatetimePersian");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
