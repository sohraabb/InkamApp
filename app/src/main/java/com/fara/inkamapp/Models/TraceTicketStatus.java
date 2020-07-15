package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class TraceTicketStatus {
    private long _id;
    private String _status;

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_status() {
        return _status;
    }

    public long get_id() {
        return _id;
    }

    public TraceTicketStatus(SoapObject input) {

        try {
            _id = Long.parseLong(input.getPropertySafelyAsString("ID"));
            _status = input.getPropertySafelyAsString("Status");

        } catch (Exception e) {
            Log.e("User Soap", e.toString());
        }
    }
}
