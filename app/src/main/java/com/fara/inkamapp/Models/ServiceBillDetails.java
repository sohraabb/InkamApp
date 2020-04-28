package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class ServiceBillDetails {

    private String _address;
    private long _amount;
    private String _billId;
    private String _billPdfUrl;
    private String _currentDate;
    private String _extraInfo;
    private String _fullName;
    private String _paymentDate;
    private String _paymentId;
    private String _previousDate;

    public void set_amount(long _amount) {
        this._amount = _amount;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public void set_billId(String _billId) {
        this._billId = _billId;
    }

    public void set_billPdfUrl(String _billPdfUrl) {
        this._billPdfUrl = _billPdfUrl;
    }

    public void set_currentDate(String _currentDate) {
        this._currentDate = _currentDate;
    }

    public void set_extraInfo(String _extraInfo) {
        this._extraInfo = _extraInfo;
    }

    public void set_fullName(String _fullName) {
        this._fullName = _fullName;
    }

    public void set_paymentDate(String _paymentDate) {
        this._paymentDate = _paymentDate;
    }

    public void set_paymentId(String _paymentId) {
        this._paymentId = _paymentId;
    }

    public void set_previousDate(String _previousDate) {
        this._previousDate = _previousDate;
    }

    public String get_extraInfo() {
        return _extraInfo;
    }

    public String get_address() {
        return _address;
    }

    public long get_amount() {
        return _amount;
    }

    public String get_billId() {
        return _billId;
    }

    public String get_billPdfUrl() {
        return _billPdfUrl;
    }

    public String get_currentDate() {
        return _currentDate;
    }

    public String get_fullName() {
        return _fullName;
    }

    public String get_paymentDate() {
        return _paymentDate;
    }

    public String get_paymentId() {
        return _paymentId;
    }

    public String get_previousDate() {
        return _previousDate;
    }

    public ServiceBillDetails(SoapObject input) {

        try {
            _address = input.getPropertySafelyAsString("Address");
            _amount = Long.parseLong(input.getPropertySafelyAsString("Amount"));
            _billId = input.getPropertySafelyAsString("BillID");
            _billPdfUrl = input.getPropertySafelyAsString("BillPdfUrl");
            _currentDate = input.getPropertySafelyAsString("CurrentDate");
            _extraInfo = input.getPropertySafelyAsString("ExtraInfo");
            _fullName = input.getPropertySafelyAsString("FullName");
            _paymentDate = input.getPropertySafelyAsString("PaymentDate");
            _paymentId = input.getPropertySafelyAsString("PaymentID");
            _previousDate = input.getPropertySafelyAsString("PreviousDate");


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
