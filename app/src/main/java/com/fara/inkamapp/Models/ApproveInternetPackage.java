package com.fara.inkamapp.Models;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ApproveInternetPackage {
    private short _status;
    private long _orderId;
    private long _refrenceNumber;
    private String _message;
    private Date _dateTime;
    private InternetData _data;
    private Error _error;

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_refrenceNumber(long _refrenceNumber) {
        this._refrenceNumber = _refrenceNumber;
    }

    public void set_status(short _status) {
        this._status = _status;
    }

    public void set_orderId(long _orderId) {
        this._orderId = _orderId;
    }

    public void set_dateTime(Date _dateTime) {
        this._dateTime = _dateTime;
    }

    public void set_error(Error _error) {
        this._error = _error;
    }

    public void set_data(InternetData _data) {
        this._data = _data;
    }

    public void set_Message(String _Message) {
        this._message = _Message;
    }

    public short get_status() {
        return _status;
    }

    public long get_orderId() {
        return _orderId;
    }

    public Date get_dateTime() {
        return _dateTime;
    }

    public Error get_error() {
        return _error;
    }

    public InternetData get_data() {
        return _data;
    }

    public String get_Message() {
        return _message;
    }

    public String get_message() {
        return _message;
    }

    public long get_refrenceNumber() {
        return _refrenceNumber;
    }

    public ApproveInternetPackage(SoapObject input) {

        try {
            _status = Short.parseShort(input.getPropertySafelyAsString("Status"));
            _orderId = Long.parseLong(input.getPropertySafelyAsString("OrderID"));
            _refrenceNumber = Long.parseLong(input.getPropertySafelyAsString("RefrenceNumber"));
            _message = input.getPropertySafelyAsString("Message");
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                _dateTime = formatter.parse(input.getPropertySafelyAsString("Date"));
            } catch (Exception ex) {
                _dateTime = new Date();
            }

            SoapObject data = (SoapObject) input.getPropertySafely("Data");
            _data = new InternetData(data);

            SoapObject error = (SoapObject) input.getPropertySafely("Error");
            _error = new Error(error);
        } catch (Exception e) {
            e.toString();
        }

    }
}
