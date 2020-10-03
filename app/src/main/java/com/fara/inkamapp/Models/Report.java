package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    private String _mobile;
    private String _refrenceNumber;
    private String _transactionNumber;
    private Date _dateTime;
    private double _amount;
    private String _statusName;
    private String _toName;
    private String _fromName;
    private String _type;
    private String _targetPhone;
    private String _internetMobile;
    private String _billID;
    private String _payID;
    private String _packageDescription;
    private String _description;
    private String _persianDateTime;

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void set_packageDescription(String _packageDescription) {
        this._packageDescription = _packageDescription;
    }

    public void set_billID(String _billID) {
        this._billID = _billID;
    }


    public void set_fromName(String _fromName) {
        this._fromName = _fromName;
    }

    public void set_internetMobile(String _internetMobile) {
        this._internetMobile = _internetMobile;
    }

    public void set_mobile(String _mobile) {
        this._mobile = _mobile;
    }

    public void set_payID(String _payID) {
        this._payID = _payID;
    }

    public void set_persianDateTime(String _persianDateTime) {
        this._persianDateTime = _persianDateTime;
    }

    public void set_refrenceNumber(String _refrenceNumber) {
        this._refrenceNumber = _refrenceNumber;
    }

    public void set_dateTime(Date _dateTime) {
        this._dateTime = _dateTime;
    }

    public void set_statusName(String _statusName) {
        this._statusName = _statusName;
    }

    public void set_targetPhone(String _targetPhone) {
        this._targetPhone = _targetPhone;
    }

    public void set_toName(String _toName) {
        this._toName = _toName;
    }

    public void set_transactionNumber(String _transactionNumber) {
        this._transactionNumber = _transactionNumber;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_description() {
        return _description;
    }

    public String get_packageDescription() {
        return _packageDescription;
    }

    public double get_amount() {
        return _amount;
    }

    public String get_billID() {
        return _billID;
    }

    public Date get_dateTime() {
        return _dateTime;
    }

    public String get_fromName() {
        return _fromName;
    }

    public String get_internetMobile() {
        return _internetMobile;
    }

    public String get_mobile() {
        return _mobile;
    }

    public String get_payID() {
        return _payID;
    }

    public String get_refrenceNumber() {
        return _refrenceNumber;
    }

    public String get_statusName() {
        return _statusName;
    }

    public String get_persianDateTime() {
        return _persianDateTime;
    }

    public String get_targetPhone() {
        return _targetPhone;
    }

    public String get_toName() {
        return _toName;
    }

    public String get_transactionNumber() {
        return _transactionNumber;
    }

    public String get_type() {
        return _type;
    }

    public Report(SoapObject input) {

        try {
            _mobile = input.getPropertySafelyAsString("Mobile");
            _refrenceNumber = input.getPropertySafelyAsString("RefrenceNumber");
            _transactionNumber = input.getPropertySafelyAsString("TransactionNumber");
            _amount = Double.parseDouble(input.getPropertySafelyAsString("Amount"));
            _statusName = input.getPropertySafelyAsString("StatusName");
            _toName = input.getPropertySafelyAsString("toName");
            _fromName = input.getPropertySafelyAsString("FromName");
            _type = input.getPropertySafelyAsString("Type");
            _targetPhone = input.getPropertySafelyAsString("TargetPhone");
            _internetMobile = input.getPropertySafelyAsString("InternetMobile");
            _billID = input.getPropertySafelyAsString("BillID");
            _payID = input.getPropertySafelyAsString("PayID");
            _packageDescription = input.getPropertySafelyAsString("PackageDescription");
            _description = input.getPropertySafelyAsString("Description");
            _persianDateTime = input.getPropertySafelyAsString("PersianDateTime");

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                _dateTime = formatter.parse(input.getPropertySafelyAsString("DateTime"));
            } catch (Exception ex) {
                _dateTime = new Date();
            }

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
