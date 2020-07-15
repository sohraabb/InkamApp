package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReserveTopupRequest {
    /// <summary>
    /// شماره  سفارش
    /// </summary>
    private String _responseMessage;
    private long _reserveNumber;
    private double _amount;
    private String _cellNumber;
    private String _cellNumbers;
    private String _reserveNumberString;
    private String _referenceNumberString;
    private String _date;


    /// <summary>
    /// نوع درگاه خرید شارژ
    /// </summary>
    private int _deviceType = 59;
    /// <summary>
    /// سرویس دهنده خدمات
    /// </summary>
    private int _operator;
    /// <summary>
    /// نوع شارژ
    /// </summary>
    private int _chargeType;
    /// <summary>
    /// شناسه بانک
    /// </summary>
    private int _bankId;
    /// <summary>
    /// قابلیت تمدید خودکار(y/n)
    /// </summary>
    private String _autoRenewal;
    /// <summary>
    /// تعداد پین شارژ درخواستی
    /// </summary>
    private int _pinCount;
    /// <summary>
    /// نام برند
    /// </summary>
    private String _merchant;
    /// <summary>
    /// کد ملی خریدار
    /// </summary>
    private String _nationalID;
    /// <summary>
    /// شماره پیگیری
    /// </summary>
    private long _referenceNumber;
    /// <summary>
    /// مبلغ کسر شده
    /// </summary>
    private double _principalAmount;
    /// <summary>
    /// کد وضعیت درخواست
    /// </summary>
    private int _responseCode;
    /// <summary>
    /// متن متناظر با کد پاسخ
    /// </summary>
//    private List<String> _responseMessage;

    /// <summary>
    /// وضعیت تراکنش
    /// </summary>
    public String _txnStatus;
    public String _redirectUR;
    /// <summary>
    /// نامه بسته اینترنت
    /// </summary>
    private String _packageDescription;
    private String _token;
    private String _pinList;
    private List<ApproveRequest> approveRequests;

    public void setApproveRequests(List<ApproveRequest> approveRequests) {
        this.approveRequests = approveRequests;
    }

    public List<ApproveRequest> getApproveRequests() {
        return approveRequests;
    }

    public void set_amount(double _amount) {
        this._amount = _amount;
    }

    public void set_autoRenewal(String _autoRenewal) {
        this._autoRenewal = _autoRenewal;
    }

    public void set_bankId(int _bankId) {
        this._bankId = _bankId;
    }

    public void set_cellNumber(String _cellNumber) {
        this._cellNumber = _cellNumber;
    }

    public void set_cellNumbers(String _cellNumbers) {
        this._cellNumbers = _cellNumbers;
    }

    public void set_chargeType(int _chargeType) {
        this._chargeType = _chargeType;
    }

    public void set_deviceType(int _deviceType) {
        this._deviceType = _deviceType;
    }

    public void set_merchant(String _merchant) {
        this._merchant = _merchant;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public void set_reserveNumber(long _reserveNumber) {
        this._reserveNumber = _reserveNumber;
    }

    public void set_nationalID(String _nationalID) {
        this._nationalID = _nationalID;
    }

    public void set_operator(int _operator) {
        this._operator = _operator;
    }

    public void set_packageDescription(String _packageDescription) {
        this._packageDescription = _packageDescription;
    }

    public void set_referenceNumber(long _peferenceNumber) {
        this._referenceNumber = _peferenceNumber;
    }

    public void set_pinCount(int _pinCount) {
        this._pinCount = _pinCount;
    }

    public void set_pinList(String _pinList) {
        this._pinList = _pinList;
    }

    public void set_principalAmount(double _principalAmount) {
        this._principalAmount = _principalAmount;
    }

    public void set_redirectUR(String _redirectUR) {
        this._redirectUR = _redirectUR;
    }

    public void set_responseCode(int _responseCode) {
        this._responseCode = _responseCode;
    }

//    public void set_responseMessage(List<String> _responseMessage) {
//        this._responseMessage = _responseMessage;
//    }

    public void set_txnStatus(String _txnStatus) {
        this._txnStatus = _txnStatus;
    }

    public double get_amount() {
        return _amount;
    }

    public String get_token() {
        return _token;
    }

    public double get_principalAmount() {
        return _principalAmount;
    }

    public int get_bankId() {
        return _bankId;
    }

    public int get_chargeType() {
        return _chargeType;
    }

    public int get_deviceType() {
        return _deviceType;
    }

    public int get_operator() {
        return _operator;
    }

    public int get_pinCount() {
        return _pinCount;
    }

    public int get_responseCode() {
        return _responseCode;
    }

//    public List<String> get_responseMessage() {
//        return _responseMessage;
//    }

    public long get_referenceNumber() {
        return _referenceNumber;
    }

    public long get_reserveNumber() {
        return _reserveNumber;
    }

    public String get_autoRenewal() {
        return _autoRenewal;
    }

    public String get_cellNumber() {
        return _cellNumber;
    }

    public String get_cellNumbers() {
        return _cellNumbers;
    }

    public String get_merchant() {
        return _merchant;
    }

    public String get_nationalID() {
        return _nationalID;
    }

    public String get_packageDescription() {
        return _packageDescription;
    }

    public String get_redirectUR() {
        return _redirectUR;
    }

    public String get_txnStatus() {
        return _txnStatus;
    }

    public String get_pinList() {
        return _pinList;
    }

    public void set_responseMessage(String _responseMessage) {
        this._responseMessage = _responseMessage;
    }

    public String get_responseMessage() {
        return _responseMessage;
    }

    public String get_reserveNumberString() {
        return _reserveNumberString;
    }

    public void set_reserveNumberString(String _reserveNumberString) {
        this._reserveNumberString = _reserveNumberString;
    }

    public String get_referenceNumberString() {
        return _referenceNumberString;
    }

    public void set_referenceNumberString(String _referenceNumberString) {
        this._referenceNumberString = _referenceNumberString;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public class ApproveRequest {
        public long _reserveNumber;
        public long _referenceNumber;

        public void set_reserveNumber(long _reserveNumber) {
            this._reserveNumber = _reserveNumber;
        }

        public void set_referenceNumber(long _referenceNumber) {
            this._referenceNumber = _referenceNumber;
        }

        public long get_reserveNumber() {
            return _reserveNumber;
        }

        public long get_referenceNumber() {
            return _referenceNumber;
        }


        public ApproveRequest(SoapObject input) {
            try {
                _reserveNumber = Long.parseLong(input.getPropertySafelyAsString("string"));
                _referenceNumber = Long.parseLong(input.getPropertySafelyAsString("string"));

            } catch (Exception e) {
                Log.e("Charge Soap", e.toString());
            }
        }
    }

    public ReserveTopupRequest() {
    }

    public ReserveTopupRequest(SoapObject input) {

        try {
            approveRequests = new ArrayList<>();

            _deviceType = Integer.parseInt(input.getPropertySafelyAsString("DeviceType"));
            _reserveNumber = Long.parseLong(input.getPropertySafelyAsString("ReserveNumber"));
            _amount = Double.parseDouble(input.getPropertySafelyAsString("Amount"));
            _cellNumber = input.getPropertySafelyAsString("CellNumber");
            _cellNumbers = input.getPropertySafelyAsString("CellNumbers");
            _operator = Integer.parseInt(input.getPropertySafelyAsString("Operator"));
            _chargeType = Integer.parseInt(input.getPropertySafelyAsString("ChargeType"));
            _bankId = Integer.parseInt(input.getPropertySafelyAsString("BankId"));
            _autoRenewal = input.getPropertySafelyAsString("AutoRenewal");
            _pinCount = Integer.parseInt(input.getPropertySafelyAsString("PinCount"));
            _merchant = input.getPropertySafelyAsString("Merchant");
            _nationalID = input.getPropertySafelyAsString("NationalID");
            _referenceNumber = Long.parseLong(input.getPropertySafelyAsString("ReferenceNumber"));
            _principalAmount = Double.parseDouble(input.getPropertySafelyAsString("PrincipalAmount"));
            _responseCode = Integer.parseInt(input.getPropertySafelyAsString("ResponseCode"));
            _reserveNumberString = input.getPropertySafelyAsString("ReserveNumberString");
            _referenceNumberString = input.getPropertySafelyAsString("ReferenceNumberString");
            SoapObject responseMessage = (SoapObject) input.getPropertySafely("ResponseMessage");
            _responseMessage = responseMessage.getPropertySafelyAsString("string");

            _txnStatus = input.getPropertySafelyAsString("TxnStatus");
            _redirectUR = input.getPropertySafelyAsString("RedirectUR");
            _packageDescription = input.getPropertySafelyAsString("PackageDescription");
            _token = input.getPropertySafelyAsString("Token");
            _pinList = input.getPropertySafelyAsString("PinList");
            _date = input.getPropertySafelyAsString("Date");
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
               // _date = formatter.parse(input.getPropertySafelyAsString("Date"));
            } catch (Exception ex) {
               // _date = new Date();
            }
        } catch (Exception e) {
            Log.e("Charge Soap", e.toString());
        }
    }

}
