package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class TrafficFinesDetails {
    private String _extensionData;
    private long _amount;
    private String _billId;
    private String _city;
    private String _dateTime;
    private String _delivery;
    private String _location;
    private String _paymentId;
    private String _type;

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_amount(long _amount) {
        this._amount = _amount;
    }

    public void set_billId(String _billId) {
        this._billId = _billId;
    }

    public void set_paymentId(String _paymentId) {
        this._paymentId = _paymentId;
    }

    public void set_city(String _city) {
        this._city = _city;
    }

    public void set_dateTime(String _dateTime) {
        this._dateTime = _dateTime;
    }

    public void set_delivery(String _delivery) {
        this._delivery = _delivery;
    }

    public void set_extensionData(String _extensionData) {
        this._extensionData = _extensionData;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String get_type() {
        return _type;
    }

    public String get_billId() {
        return _billId;
    }

    public String get_paymentId() {
        return _paymentId;
    }

    public long get_amount() {
        return _amount;
    }

    public String get_city() {
        return _city;
    }

    public String get_dateTime() {
        return _dateTime;
    }

    public String get_delivery() {
        return _delivery;
    }

    public String get_extensionData() {
        return _extensionData;
    }

    public String get_location() {
        return _location;
    }

    public TrafficFinesDetails(SoapObject input) {
        try {
            _extensionData = input.getPropertySafelyAsString("ExtensionData");
            _amount = Long.parseLong(input.getPropertySafelyAsString("Amount"));
            _billId = input.getPropertySafelyAsString("BillID");
            _city = input.getPropertySafelyAsString("City");
            _dateTime = input.getPropertySafelyAsString("DateTime");
            _delivery = input.getPropertySafelyAsString("Delivery");
            _location = input.getPropertySafelyAsString("Location");
            _paymentId = input.getPropertySafelyAsString("PaymentID");
            _type = input.getPropertySafelyAsString("Type");

        } catch (Exception e) {
            Log.e("TrafficFines Soap", e.toString());
        }
    }
}
