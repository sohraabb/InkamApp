package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class BookTicket {
    private String ID;
    private String PaymentEndpoint;
    private Error Error;
    private long OrderID;
    private String Token;

    public void setError(Error error) {
        this.Error = error;
    }

    public void setToken(String token) {
        this.Token = token;
    }

    public void setOrderID(long orderID) {
        this.OrderID = orderID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPaymentEndpoint(String paymentEndpoint) {
        this.PaymentEndpoint = paymentEndpoint;
    }

    public Error getError() {
        return Error;
    }

    public String getToken() {
        return Token;
    }

    public long getOrderID() {
        return OrderID;
    }

    public String getID() {
        return ID;
    }

    public String getPaymentEndpoint() {
        return PaymentEndpoint;
    }

    public BookTicket(SoapObject input) {
        try {
            ID = input.getPropertySafelyAsString("ID");
            PaymentEndpoint = input.getPropertySafelyAsString("PaymentEndpoint");

            SoapObject error = (SoapObject) input.getPropertySafely("Error");
            Error = new Error(error);

            OrderID = Long.parseLong(input.getPropertySafelyAsString("OrderID"));
            Token = input.getPropertySafelyAsString("Token");

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }
}
