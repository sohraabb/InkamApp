package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Fund {
    private double _balance;
    private int _userChanceCount;
    private int _totalChanceCount;
    private Date _dateTime;
    private String _persianDate;

    public void set_dateTime(Date _dateTime) {
        this._dateTime = _dateTime;
    }

    public void set_balance(double _balance) {
        this._balance = _balance;
    }

    public void set_persianDate(String _persianDate) {
        this._persianDate = _persianDate;
    }

    public void set_totalChanceCount(int _totalChanceCount) {
        this._totalChanceCount = _totalChanceCount;
    }

    public void set_userChanceCount(int _userChanceCount) {
        this._userChanceCount = _userChanceCount;
    }

    public String get_persianDate() {
        return _persianDate;
    }

    public Date get_dateTime() {
        return _dateTime;
    }

    public double get_balance() {
        return _balance;
    }

    public int get_totalChanceCount() {
        return _totalChanceCount;
    }

    public int get_userChanceCount() {
        return _userChanceCount;
    }

    public Fund(SoapObject input) {
        try {
            _balance = Double.parseDouble(input.getPropertySafelyAsString("Balance"));
            _userChanceCount = Integer.parseInt(input.getPropertySafelyAsString("UserChanceCount"));
            _totalChanceCount = Integer.parseInt(input.getPropertySafelyAsString("TotalChanceCount"));
            _persianDate = input.getPropertySafelyAsString("PersianDate");


            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                _dateTime = formatter.parse(input.getPropertySafelyAsString("Datetime"));
            } catch (Exception ex) {
                _dateTime = new Date();
            }

        } catch (Exception e) {
            Log.e("FundReport Soap", e.toString());
        }
    }

}
