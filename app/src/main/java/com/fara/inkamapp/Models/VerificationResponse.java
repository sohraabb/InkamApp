package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class VerificationResponse {

    private boolean _verificationCodeResponse;

    public void set_verificationCodeResponse(boolean _verificationCodeResponse) {
        this._verificationCodeResponse = _verificationCodeResponse;
    }

    public boolean is_verificationCodeResponse() {
        return _verificationCodeResponse;
    }

    public VerificationResponse(SoapObject input) {

        try {
            _verificationCodeResponse = Boolean.parseBoolean(input.getPropertySafelyAsString("CheckVerificationCodeResult"));

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
