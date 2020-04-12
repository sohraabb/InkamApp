package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class User {

    private String _id;
    private String _username;
    private String _password;
    private String _firstName;
    private String _lastName;
    private String _email;
    private String _phone;
    private String _melliCode;
    private String _profilePicture;
    private String _cityId;
    private String _provinceId;
    private String _presentorId;
    private String _userTypeId;
    private String _languageId;
    private double _point;
    private double _todayProfit;
    private double _cashOfWallet;
    private String _creditCard;
    private String _sheba;
    private String _bankName;
    private String _storeName;
    private int _fieldId;
    private String _creditCardOwner;
    private String _fieldName;
    private int _acquaintanceId;
    private String _acquaintanceName;
    private String _address;
    private String _postalCode;
    private String _token;
    private boolean _isUser;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public void set_cityId(String _cityId) {
        this._cityId = _cityId;
    }

    public void set_languageId(String _languageId) {
        this._languageId = _languageId;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public void set_melliCode(String _melliCode) {
        this._melliCode = _melliCode;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public void set_cashOfWallet(double _cashOfWallet) {
        this._cashOfWallet = _cashOfWallet;
    }

    public void set_point(double _point) {
        this._point = _point;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public void set_presentorId(String _presentorId) {
        this._presentorId = _presentorId;
    }

    public void set_bankName(String _bankName) {
        this._bankName = _bankName;
    }

    public void set_profilePicture(String _profilePicture) {
        this._profilePicture = _profilePicture;
    }

    public void set_provinceId(String _provinceId) {
        this._provinceId = _provinceId;
    }

    public void set_todayProfit(double _todayProfit) {
        this._todayProfit = _todayProfit;
    }

    public void set_userTypeId(String _userTypeId) {
        this._userTypeId = _userTypeId;
    }

    public String get_id() {
        return _id;
    }

    public String get_cityId() {
        return _cityId;
    }

    public double get_cashOfWallet() {
        return _cashOfWallet;
    }

    public String get_email() {
        return _email;
    }

    public double get_todayProfit() {
        return _todayProfit;
    }

    public String get_bankName() {
        return _bankName;
    }

    public String get_firstName() {
        return _firstName;
    }

    public String get_languageId() {
        return _languageId;
    }

    public String get_lastName() {
        return _lastName;
    }

    public String get_creditCard() {
        return _creditCard;
    }

    public String get_melliCode() {
        return _melliCode;
    }

    public String get_password() {
        return _password;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_presentorId() {
        return _presentorId;
    }

    public String get_profilePicture() {
        return _profilePicture;
    }

    public double get_point() {
        return _point;
    }

    public String get_username() {
        return _username;
    }

    public String get_provinceId() {
        return _provinceId;
    }

    public String get_userTypeId() {
        return _userTypeId;
    }

    public boolean is_isUser() {
        return _isUser;
    }

    public void set_isUser(boolean _isUser) {
        this._isUser = _isUser;
    }

    public void set_acquaintanceId(int _acquaintanceId) {
        this._acquaintanceId = _acquaintanceId;
    }

    public void set_creditCard(String _creditCard) {
        this._creditCard = _creditCard;
    }

    public void set_acquaintanceName(String _acquaintanceName) {
        this._acquaintanceName = _acquaintanceName;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public void set_creditCardOwner(String _creditCardOwner) {
        this._creditCardOwner = _creditCardOwner;
    }

    public void set_fieldId(int _fieldId) {
        this._fieldId = _fieldId;
    }

    public void set_fieldName(String _fieldName) {
        this._fieldName = _fieldName;
    }

    public void set_postalCode(String _postalCode) {
        this._postalCode = _postalCode;
    }

    public void set_sheba(String _sheba) {
        this._sheba = _sheba;
    }

    public void set_storeName(String _storeName) {
        this._storeName = _storeName;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public int get_fieldId() {
        return _fieldId;
    }

    public String get_sheba() {
        return _sheba;
    }

    public int get_acquaintanceId() {
        return _acquaintanceId;
    }

    public String get_acquaintanceName() {
        return _acquaintanceName;
    }

    public String get_storeName() {
        return _storeName;
    }

    public String get_address() {
        return _address;
    }

    public String get_creditCardOwner() {
        return _creditCardOwner;
    }

    public String get_fieldName() {
        return _fieldName;
    }

    public String get_postalCode() {
        return _postalCode;
    }

    public String get_token() {
        return _token;
    }

    public User(SoapObject input) {

        try {
            _id = input.getPropertySafelyAsString("ID");
            _username = input.getPropertySafelyAsString("UserName");
            _password = input.getPropertySafelyAsString("Password");
            _firstName = input.getPropertySafelyAsString("FirstName");
            _lastName = input.getPropertySafelyAsString("LastName");
            _email = input.getPropertySafelyAsString("Email");
            _phone = input.getPropertySafelyAsString("Phone");
            _melliCode = input.getPropertySafelyAsString("MelliCode");
            _profilePicture = input.getPropertySafelyAsString("ProfilePicURL");
            _cityId = input.getPropertySafelyAsString("CityID");
            _provinceId = input.getPropertySafelyAsString("ProvinceID");
            _presentorId = input.getPropertySafelyAsString("PresentorID");
            _userTypeId = input.getPropertySafelyAsString("UserTypeID");
            _languageId = input.getPropertySafelyAsString("LanguageID");
            _point = Double.parseDouble(input.getPropertySafelyAsString("Point"));
            _todayProfit = Double.parseDouble(input.getPropertySafelyAsString("TodayProfit"));
            _cashOfWallet = Double.parseDouble(input.getPropertySafelyAsString("CashOfWallet"));
            _creditCard = input.getPropertySafelyAsString("CreditCard");
            _sheba = input.getPropertySafelyAsString("Sheba");
            _bankName = input.getPropertySafelyAsString("BankName");
            _storeName = input.getPropertySafelyAsString("StoreName");
            _fieldId = Integer.parseInt(input.getPropertySafelyAsString("FieldID"));
            _creditCardOwner = input.getPropertySafelyAsString("CreditCardOwner");
            _fieldName = input.getPropertySafelyAsString("FieldName");
            _acquaintanceId = Integer.parseInt(input.getPropertySafelyAsString("AcquaintanceID"));
            _acquaintanceName = input.getPropertySafelyAsString("AcquaintanceName");
            _address = input.getPropertySafelyAsString("Address");
            _postalCode = input.getPropertySafelyAsString("PostalCode");
            _token = input.getPropertySafelyAsString("Token");
            _isUser = Boolean.parseBoolean(input.getPropertySafelyAsString("IsUser"));

        } catch (Exception e) {
            Log.e("User Soap", e.toString());
        }
}
    }
