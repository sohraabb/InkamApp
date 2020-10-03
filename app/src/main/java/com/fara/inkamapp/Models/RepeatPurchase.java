package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class RepeatPurchase {
    private String _id;
    private String _userID;
    private String _mobile;
    private String _chargeTypeName;
    private String _code;
    private String _type;
    private int _operator;
    private int _chargeType;
    private int _dataPlanType;
    private double _amount;
    private String _amountString;


    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public void set_chargeType(int _chargeType) {
        this._chargeType = _chargeType;
    }

    public void set_chargeTypeName(String _chargeTypeName) {
        this._chargeTypeName = _chargeTypeName;
    }

    public void set_code(String _code) {
        this._code = _code;
    }

    public void set_dataPlanType(int _dataPlanType) {
        this._dataPlanType = _dataPlanType;
    }

    public void set_mobile(String _mobile) {
        this._mobile = _mobile;
    }

    public void set_operator(int _operator) {
        this._operator = _operator;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_userID(String _userID) {
        this._userID = _userID;
    }

    public String get_userID() {
        return _userID;
    }

    public String get_id() {
        return _id;
    }

    public double get_amount() {
        return _amount;
    }

    public String get_chargeTypeName() {
        return _chargeTypeName;
    }

    public int get_chargeType() {
        return _chargeType;
    }

    public int get_dataPlanType() {
        return _dataPlanType;
    }

    public int get_operator() {
        return _operator;
    }

    public String get_code() {
        return _code;
    }

    public String get_mobile() {
        return _mobile;
    }

    public String get_type() {
        return _type;
    }

    public void set_amountString(String _amountString) {
        this._amountString = _amountString;
    }

    public String get_amountString() {
        return _amountString;
    }

    public RepeatPurchase(SoapObject input) {
        try {
            _id = input.getPropertySafelyAsString("ID");
            _userID = input.getPropertySafelyAsString("UserID");
            _mobile = input.getPropertySafelyAsString("Mobile");
            _chargeTypeName = input.getPropertySafelyAsString("ChargTypeName");
            _code = input.getPropertySafelyAsString("Code");
            _type = input.getPropertySafelyAsString("Type");
            _operator = Integer.parseInt(input.getPropertySafelyAsString("Operator"));
            _chargeType = Integer.parseInt(input.getPropertySafelyAsString("ChargeType"));
            _dataPlanType = Integer.parseInt(input.getPropertySafelyAsString("DataPlanType"));
            _amount = Double.parseDouble(input.getPropertySafelyAsString("Amount"));
            _amountString = input.getPropertySafelyAsString("AmountString");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
