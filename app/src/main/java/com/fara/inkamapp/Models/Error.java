package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Error {
    private String _Code;
    private String _Message;
    private String _code;
    private String _message;

    public void set_code(String _code) {
        this._code = _code;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_Code(String _Code) {
        this._Code = _Code;
    }

    public void set_Message(String _Message) {
        this._Message = _Message;
    }

    public String get_code() {
        return _code;
    }

    public String get_message() {
        return _message;
    }

    public String get_Code() {
        return _Code;
    }

    public String get_Message() {
        return _Message;
    }

    public Error(SoapObject input) {
        try {
            _Code = input.getPropertySafelyAsString("Code");
            _Message = input.getPropertySafelyAsString("Message");
            _code = input.getPropertySafelyAsString("code");
            _message = input.getPropertySafelyAsString("message");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
