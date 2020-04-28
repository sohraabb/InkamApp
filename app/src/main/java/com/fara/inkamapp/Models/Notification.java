package com.fara.inkamapp.Models;

import android.net.ParseException;
import android.util.Log;
import org.ksoap2.serialization.SoapObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {
    private String _id;
    private String _userID;
    private String _title;
    private String _message;
    private boolean _status;
    private String _dateTime;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_status(boolean _status) {
        this._status = _status;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_dateTime(String _dateTime) {
        this._dateTime = _dateTime;
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

    public String get_dateTime() {
        return _dateTime;
    }

    public String get_title() {
        return _title;
    }

    public String get_userID() {
        return _userID;
    }
    public Notification(SoapObject input){
        try {
            _id = input.getPropertySafelyAsString("ID");
            _userID = input.getPropertySafelyAsString("UserID");
            _title = input.getPropertySafelyAsString("Title");
            _message = input.getPropertySafelyAsString("Message");
            _status = Boolean.parseBoolean(input.getPropertySafelyAsString("Status"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date = new Date();
                _dateTime = dateFormat.format(input.getPropertySafelyAsString("Datetime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
