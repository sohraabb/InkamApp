package com.fara.inkamapp.Models;

public class PaymentResult {
    private Error Error;
    private Data Data;
    private String Status;
    private String Message;
    private long OrderID;


    public PaymentResult(Error error, Data data, String status, String message, long orderID) {
        Error = error;
        Data = data;
        Status = status;
        Message = message;
        OrderID = orderID;
    }

    public Error getError() {
        return Error;
    }

    public void setError(Error error) {
        Error = error;
    }

    public Data getData() {
        return Data;
    }

    public void setData(com.fara.inkamapp.Models.Data data) {
        Data = data;
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

    public long getOrderID() {
        return OrderID;
    }

    public void setOrderID(long orderID) {
        OrderID = orderID;
    }
}
