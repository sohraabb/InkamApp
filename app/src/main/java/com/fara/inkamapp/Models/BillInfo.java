package com.fara.inkamapp.Models;

import android.net.ParseException;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BillInfo {
    private long _amount;
    private String _billType;
    private String _utilityCode;
    private String _subUtilityCode;
    private String _companyName;
    private short _status;
    private String _statusDescription;
    private String _requestDateTime;

    public void set_status(short _status) {
        this._status = _status;
    }

    public void set_amount(long _amount) {
        this._amount = _amount;
    }

    public void set_billType(String _billType) {
        this._billType = _billType;
    }

    public void set_companyName(String _companyName) {
        this._companyName = _companyName;
    }

    public void set_requestDateTime(String _requestDateTime) {
        this._requestDateTime = _requestDateTime;
    }

    public void set_utilityCode(String _utilityCode) {
        this._utilityCode = _utilityCode;
    }

    public void set_statusDescription(String _statusDescription) {
        this._statusDescription = _statusDescription;
    }

    public void set_subUtilityCode(String _subUtilityCode) {
        this._subUtilityCode = _subUtilityCode;
    }

    public long get_amount() {
        return _amount;
    }

    public String get_requestDateTime() {
        return _requestDateTime;
    }

    public short get_status() {
        return _status;
    }

    public String get_billType() {
        return _billType;
    }

    public String get_companyName() {
        return _companyName;
    }

    public String get_statusDescription() {
        return _statusDescription;
    }

    public String get_subUtilityCode() {
        return _subUtilityCode;
    }

    public String get_utilityCode() {
        return _utilityCode;
    }

    public BillInfo(SoapObject input) {
        try {
            _amount = Long.parseLong(input.getPropertySafelyAsString("Amount"));
            _billType = input.getPropertySafelyAsString("BillType");
            _utilityCode = input.getPropertySafelyAsString("UtilityCode");
            _subUtilityCode = input.getPropertySafelyAsString("SubUtilityCode");
            _companyName = input.getPropertySafelyAsString("CompanyName");
            _status = Short.parseShort(input.getPropertySafelyAsString("Status"));
            _statusDescription = input.getPropertySafelyAsString("StatusDescription");


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date = new Date();
                _requestDateTime = dateFormat.format(input.getPropertySafelyAsString("RequestDateTime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
