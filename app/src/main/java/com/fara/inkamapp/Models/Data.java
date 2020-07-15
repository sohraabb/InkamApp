package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.Date;

public class Data {
    private String Token;
    private String OrderId;
    private String ExpireDate;
    private String Status;
    private String Message;
    private String Date;

    /* public void set_message(String _message) {
         this._message = _message;
     }

     public void set_status(String _status) {
         this._status = _status;
     }

     public void set_token(String _token) {
         this._token = _token;
     }

     public void set_expireDate(String _expireDate) {
         this._expireDate = _expireDate;
     }

     public String get_message() {
         return _message;
     }

     public String get_status() {
         return _status;
     }

     public String get_token() {
         return _token;
     }

     public String get_expireDate() {
         return _expireDate;
     }
 */
    public Data(SoapObject input){

        try {
            Status = input.getPropertySafelyAsString("Status");
            Message = input.getPropertySafelyAsString("Message");
            Token = input.getPropertySafelyAsString("Token");
            ExpireDate = input.getPropertySafelyAsString("ExpireDate");
            Date = input.getPropertySafelyAsString("Date");


        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
    public Data(SoapObject input, String expireDate, String status, String message){
        ExpireDate = expireDate;
        Status = status;
        Message = message;
        try {
            Token = input.getPropertySafelyAsString("Token");
            ExpireDate = input.getPropertySafelyAsString("ExpireDate");
            Status = input.getPropertySafelyAsString("Status");
            Message = input.getPropertySafelyAsString("Message");
            OrderId = input.getPropertySafelyAsString("OrderId");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
