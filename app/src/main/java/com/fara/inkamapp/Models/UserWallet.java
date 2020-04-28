package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class UserWallet {

    private String _id;
    private String _accountId;
    private double _balance;
    private double _todayProfit;
    private String _name;
    private String _status;
    private String _message;

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_accountId(String _accountId) {
        this._accountId = _accountId;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_todayProfit(double _todayProfit) {
        this._todayProfit = _todayProfit;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_balance(double _balance) {
        this._balance = _balance;
    }

    public String get_name() {
        return _name;
    }

    public String get_accountId() {
        return _accountId;
    }

    public String get_id() {
        return _id;
    }

    public double get_todayProfit() {
        return _todayProfit;
    }

    public String get_status() {
        return _status;
    }

    public String get_message() {
        return _message;
    }

    public double get_balance() {
        return _balance;
    }

    public UserWallet(SoapObject input) {

        try {
            _id = input.getPropertySafelyAsString("ID");
            _accountId = input.getPropertySafelyAsString("AccountID");
            _balance = Double.parseDouble(input.getPropertySafelyAsString("Balance"));
            _todayProfit = Double.parseDouble(input.getPropertySafelyAsString("TodayProfit"));
            _name = input.getPropertySafelyAsString("Name");
            _status = input.getPropertySafelyAsString("Status");
            _message = input.getPropertySafelyAsString("Message");

        } catch (Exception e) {
            Log.e("User Soap", e.toString());
        }
    }
}
