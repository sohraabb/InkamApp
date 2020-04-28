package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Passengers {
    private String _firstName;
    private String _lastName;
    private int _seatNumber;
    private String _nationalCode;
    private String _gender;

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public void set_nationalCode(String _nationalCode) {
        this._nationalCode = _nationalCode;
    }

    public void set_seatNumber(int _seatNumber) {
        this._seatNumber = _seatNumber;
    }

    public int get_seatNumber() {
        return _seatNumber;
    }

    public String get_lastName() {
        return _lastName;
    }

    public String get_firstName() {
        return _firstName;
    }

    public String get_gender() {
        return _gender;
    }

    public String get_nationalCode() {
        return _nationalCode;
    }

    public Passengers(SoapObject input) {

        try {
            _firstName = input.getPropertySafelyAsString("FirstName");
            _lastName = input.getPropertySafelyAsString("LastName");
            _seatNumber = Integer.parseInt(input.getPropertySafelyAsString("SeatNumber"));
            _nationalCode = input.getPropertySafelyAsString("NationalCode");
            _gender = input.getPropertySafelyAsString("Gender");

        } catch (Exception e) {
            Log.e("ResponseStatus Soap", e.toString());
        }
    }
}
