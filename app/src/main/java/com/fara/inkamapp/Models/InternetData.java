package com.fara.inkamapp.Models;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InternetData {
    private String _token;
    private Date _expDate;
    private short _status;
    private String _message;
    private boolean _isValid;
    private long _amount;
    private String _cardNumber;
    private String _hashedCardNumber;
    private String _merchantName;
    private long _orderId;
    private Date _requestDateTime;
    private long _rrn;
    private String _statusDescription;
    private int _terminalNo;
    private int _traceNo;
    private String _billId;
    private String _payId;

    public void set_dateTime(Date _dateTime) {
        this._expDate = _dateTime;
    }

    public void set_amount(long _amount) {
        this._amount = _amount;
    }

    public void set_isValid(boolean _isValid) {
        this._isValid = _isValid;
    }

    public void set_billId(String _billId) {
        this._billId = _billId;
    }

    public void set_cardNumber(String _cardNumber) {
        this._cardNumber = _cardNumber;
    }

    public void set_hashedCardNumber(String _hashedCardNumber) {
        this._hashedCardNumber = _hashedCardNumber;
    }

    public void set_merchantName(String _merchantName) {
        this._merchantName = _merchantName;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_orderId(long _orderId) {
        this._orderId = _orderId;
    }

    public void set_requestDateTime(Date _requestDateTime) {
        this._requestDateTime = _requestDateTime;
    }

    public void set_rrn(long _rrn) {
        this._rrn = _rrn;
    }

    public void set_status(short _status) {
        this._status = _status;
    }

    public void set_statusDescription(String _statusDescription) {
        this._statusDescription = _statusDescription;
    }

    public void set_terminalNo(int _terminalNo) {
        this._terminalNo = _terminalNo;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void set_traceNo(int _traceNo) {
        this._traceNo = _traceNo;
    }

    public void setPayId(String payId) {
        this._payId = payId;
    }

    public Date get_dateTime() {
        return _expDate;
    }

    public Date get_requestDateTime() {
        return _requestDateTime;
    }

    public int get_terminalNo() {
        return _terminalNo;
    }

    public int get_traceNo() {
        return _traceNo;
    }

    public long get_amount() {
        return _amount;
    }

    public long get_orderId() {
        return _orderId;
    }

    public long get_rrn() {
        return _rrn;
    }

    public short get_status() {
        return _status;
    }

    public String get_billId() {
        return _billId;
    }

    public String get_cardNumber() {
        return _cardNumber;
    }

    public String get_hashedCardNumber() {
        return _hashedCardNumber;
    }

    public String get_merchantName() {
        return _merchantName;
    }

    public String get_message() {
        return _message;
    }

    public String get_statusDescription() {
        return _statusDescription;
    }

    public String get_token() {
        return _token;
    }

    public String getPayId() {
        return _payId;
    }

    public boolean is_isValid() {
        return _isValid;
    }

    public InternetData(SoapObject input) {
        try {
            _token = input.getPropertySafelyAsString("Token");
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                _expDate = formatter.parse(input.getPropertySafelyAsString("ExpireDate"));
                _requestDateTime = formatter.parse(input.getPropertySafelyAsString("RequestDateTime"));
            } catch (Exception ex) {
                _expDate = new Date();
                _requestDateTime = new Date();
            }
            _status = Short.parseShort(input.getPropertySafelyAsString("Status"));
            _message = input.getPropertySafelyAsString("Message");
            _isValid = Boolean.parseBoolean(input.getPropertySafelyAsString("isValid"));
            _amount = Long.parseLong(input.getPropertySafelyAsString("Amount"));
            _cardNumber = input.getPropertySafelyAsString("CardNumber");
            _hashedCardNumber = input.getPropertySafelyAsString("HashedCardNumber");
            _orderId = Long.parseLong(input.getPropertySafelyAsString("OrderId"));
            _rrn = Long.parseLong(input.getPropertySafelyAsString("RRN"));
            _statusDescription = input.getPropertySafelyAsString("StatusDescription");
            _terminalNo = Integer.parseInt(input.getPropertySafelyAsString("TerminalNo"));
            _traceNo = Integer.parseInt(input.getPropertySafelyAsString("TraceNo"));
            _billId = input.getPropertySafelyAsString("BillId");
            _payId = input.getPropertySafelyAsString("PayId");

        } catch (Exception e) {
            e.toString();
        }

    }
}
