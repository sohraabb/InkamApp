package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class PhoneBillFinalTermInfo {
    private long _amount;
    private String _billId;
    private String _extraInfo;
    private String _paymentId;

    public void set_paymentId(String _paymentId) {
        this._paymentId = _paymentId;
    }

    public void set_extraInfo(String _extraInfo) {
        this._extraInfo = _extraInfo;
    }

    public void set_billId(String _billId) {
        this._billId = _billId;
    }

    public void set_amount(long _amount) {
        this._amount = _amount;
    }

    public String get_paymentId() {
        return _paymentId;
    }

    public String get_billId() {
        return _billId;
    }

    public long get_amount() {
        return _amount;
    }

    public String get_extraInfo() {
        return _extraInfo;
    }

    public PhoneBillFinalTermInfo(SoapObject input) {

        try {
            _amount = Long.parseLong(input.getPropertySafelyAsString("Amount"));
            _billId = input.getPropertySafelyAsString("BillID");
            _extraInfo = input.getPropertySafelyAsString("ExtraInfo");
            _paymentId = input.getPropertySafelyAsString("PaymentID");


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
