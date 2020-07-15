package com.fara.inkamapp.Models;

public class PaymentResponseBase {
        public short Status;
        public String Message;
        public Object Data;

    public void setData(Object data) {
        Data = data;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setStatus(short status) {
        Status = status;
    }

    public Object getData() {
        return Data;
    }

    public short getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}
