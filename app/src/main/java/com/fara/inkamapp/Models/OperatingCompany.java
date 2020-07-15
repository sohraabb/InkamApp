package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class OperatingCompany {
    private String Code ;
    private String Name ;
    public OperatingCompany(SoapObject input){
        try {
            Code = input.getPropertySafelyAsString("Code");
            Name = input.getPropertySafelyAsString("Name");


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
