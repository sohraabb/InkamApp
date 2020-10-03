package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenResponse {
    private Date _expDate;
    private int _uID;
    private String _token;
    private int _storeID;
    private int _companyID;
    private boolean _isValid;
    private String _aesKey;
    private String _connectionString;
    private String _companyCode;
    private String _storeCode;
    private Date _lastActionDate;
    private String _message;

    public void set_isValid(boolean _isValid) {
        this._isValid = _isValid;
    }

    public boolean is_isValid() {
        return _isValid;
    }

    public TokenResponse(SoapObject input) {

        try {
            _isValid = Boolean.parseBoolean(input.getPropertySafelyAsString("isValid"));

        } catch (Exception e) {
            Log.e("User Soap", e.toString());
        }
    }
}
