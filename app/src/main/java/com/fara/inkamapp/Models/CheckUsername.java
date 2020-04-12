package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class CheckUsername {

    private int _checkUserNameResult;

    public void set_checkUserNameResult(int _checkUserNameResult) {
        this._checkUserNameResult = _checkUserNameResult;
    }

    public int get_checkUserNameResult() {
        return _checkUserNameResult;
    }

    public CheckUsername(SoapObject input) {

        try {
            _checkUserNameResult = Integer.parseInt(input.getPropertySafelyAsString("ChekUserNameResult"));

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
