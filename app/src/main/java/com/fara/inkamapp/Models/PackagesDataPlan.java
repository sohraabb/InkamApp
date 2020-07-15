package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class PackagesDataPlan {
    private int _id;
    private double _priceWithoutTax;
    private double _priceWithTax;
    private int _period;
    private String _title;
    private int _operator;
    private String _autoRenewal;
    private int _dataPlanType;

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_operator(int _operator) {
        this._operator = _operator;
    }

    public void set_autoRenewal(String _autoRenewal) {
        this._autoRenewal = _autoRenewal;
    }

    public void set_dataPlanType(int _dataPlanType) {
        this._dataPlanType = _dataPlanType;
    }

    public void set_period(int _period) {
        this._period = _period;
    }

    public void set_priceWithoutTax(double _priceWithoutTax) {
        this._priceWithoutTax = _priceWithoutTax;
    }

    public void set_priceWithTax(double _priceWithTax) {
        this._priceWithTax = _priceWithTax;
    }

    public String get_title() {
        return _title;
    }

    public String get_autoRenewal() {
        return _autoRenewal;
    }

    public int get_operator() {
        return _operator;
    }

    public double get_priceWithoutTax() {
        return _priceWithoutTax;
    }

    public double get_priceWithTax() {
        return _priceWithTax;
    }

    public int get_dataPlanType() {
        return _dataPlanType;
    }

    public int get_id() {
        return _id;
    }

    public int get_period() {
        return _period;
    }

    public PackagesDataPlan(SoapObject input) {
        try {
            _id = Integer.parseInt(input.getPropertySafelyAsString("Id"));
            _priceWithoutTax = Double.parseDouble(input.getPropertySafelyAsString("PriceWithoutTax"));
            _priceWithTax = Double.parseDouble(input.getPropertySafelyAsString("PriceWithTax"));
            _period = Integer.parseInt(input.getPropertySafelyAsString("Period"));
            _title = input.getPropertySafelyAsString("Title");
            _operator = Integer.parseInt(input.getPropertySafelyAsString("Operator"));
            _autoRenewal = input.getPropertySafelyAsString("AutoRenewal");
            _dataPlanType = Integer.parseInt(input.getPropertySafelyAsString("DataPlanType"));

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
