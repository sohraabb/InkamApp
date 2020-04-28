package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class BookTicket {
    private String _id;
    private String _paymentEndpoint;
    private Error _error;
    private long _orderId;
    private String _token;

    public void set_error(Error _error) {
        this._error = _error;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void set_orderId(long _orderId) {
        this._orderId = _orderId;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_paymentEndpoint(String _paymentEndpoint) {
        this._paymentEndpoint = _paymentEndpoint;
    }

    public Error get_error() {
        return _error;
    }

    public String get_token() {
        return _token;
    }

    public long get_orderId() {
        return _orderId;
    }

    public String get_id() {
        return _id;
    }

    public String get_paymentEndpoint() {
        return _paymentEndpoint;
    }

    public BookTicket(SoapObject input) {
        try {
            _id = input.getPropertySafelyAsString("ID");
            _paymentEndpoint = input.getPropertySafelyAsString("PaymentEndpoint");

            SoapObject error = (SoapObject) input.getPropertySafely("Error");
            _error = new Error(error);

            _orderId = Long.parseLong(input.getPropertySafelyAsString("OrderID"));
            _token = input.getPropertySafelyAsString("Token");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
