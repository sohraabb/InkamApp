package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class UserCard {

    private String _id;
    private String _bankId;
    private String _userId;
    private String _cardNumber;
    private String _expDate;
    private boolean _isDefault;
    private String _bankName;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_bankId(String _bankId) {
        this._bankId = _bankId;
    }

    public void set_cardNumber(String _cardNumber) {
        this._cardNumber = _cardNumber;
    }

    public void set_bankName(String _bankName) {
        this._bankName = _bankName;
    }

    public void set_expDate(String _expDate) {
        this._expDate = _expDate;
    }

    public void set_isDefault(boolean _isDefault) {
        this._isDefault = _isDefault;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public String get_expDate() {
        return _expDate;
    }

    public String get_userId() {
        return _userId;
    }

    public String get_id() {
        return _id;
    }

    public String get_bankName() {
        return _bankName;
    }

    public String get_bankId() {
        return _bankId;
    }

    public String get_cardNumber() {
        return _cardNumber;
    }

    public UserCard(SoapObject input) {
        try {
            _id = input.getPropertySafelyAsString("ID");
            _bankId = input.getPropertySafelyAsString("BankID");
            _userId = input.getPropertySafelyAsString("UserID");
            _cardNumber = input.getPropertySafelyAsString("CardNumber");
            _expDate = input.getPropertySafelyAsString("ExpDate");
            _isDefault = Boolean.parseBoolean(input.getPropertySafelyAsString("IsDefault"));
            _bankName = input.getPropertySafelyAsString("BankName");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }

}
