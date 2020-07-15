package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    private String ID;
    private String UserName;
    private String Password;
    private String FirstName;
    private String LastName;
    private String Email;
    private String Phone;
    private String MelliCode;
    private String ProfilePicURL;
    private String CityID;
    private String CityName;
    private String ProvinceID;
    private String PresentorID;
    private String UserTypeID;
    private String LanguageID;
    private double Point;
    private double TodayProfit;
    private double CashOfWallet;
    private String CreditCard;
    private String Sheba;
    private String BankName;
    private String StoreName;
    private int FieldID;
    private String CreditCardOwner;
    private String FieldName;
    private int AcquaintanceID;
    private String AcquaintanceName;
    private String Address;
    private String PostalCode;
    private String Token;
    private boolean IsUser;
    private int UserCount;
    private String perenestorCode;
    private boolean AgencyRequest;
    private int ChanceCount;
    private double Income;
    private String RegisteryDate;
    private String RegisteryDatePersian;
    private Date ExpirationDate;


    public void setExpirationDate(Date expirationDate) {
        this.ExpirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return ExpirationDate;
    }

    public void set_tokenExpirationDate(String expirationDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ExpirationDate = dateFormat.parse(expirationDate);
        } catch (Exception e) {
            ExpirationDate = new Date();
        }
    }

    public void setUserCount(int userCount) {
        this.UserCount = userCount;
    }

    public void setAgencyRequest(boolean agencyRequest) {
        this.AgencyRequest = agencyRequest;
    }

    public void setChanceCount(int chanceCount) {
        this.ChanceCount = chanceCount;
    }

    public void setIncome(double income) {
        this.Income = income;
    }

    public void setPerenestorCode(String perenestorCode) {
        this.perenestorCode = perenestorCode;
    }

    public void setRegisteryDate(String registeryDate) {
        this.RegisteryDate = registeryDate;
    }

    public void setRegisteryDatePersian(String registeryDatePersian) {
        this.RegisteryDatePersian = registeryDatePersian;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public void setCityID(String cityID) {
        this.CityID = cityID;
    }

    public void setLanguageID(String languageID) {
        this.LanguageID = languageID;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public void setMelliCode(String melliCode) {
        this.MelliCode = melliCode;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public void setCashOfWallet(double cashOfWallet) {
        this.CashOfWallet = cashOfWallet;
    }

    public void setPoint(double point) {
        this.Point = point;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public void setPresentorID(String presentorID) {
        this.PresentorID = presentorID;
    }

    public void setBankName(String bankName) {
        this.BankName = bankName;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.ProfilePicURL = profilePicURL;
    }

    public void setProvinceID(String provinceID) {
        this.ProvinceID = provinceID;
    }

    public void setTodayProfit(double todayProfit) {
        this.TodayProfit = todayProfit;
    }

    public void setUserTypeID(String userTypeID) {
        this.UserTypeID = userTypeID;
    }

    public String getID() {
        return ID;
    }

    public String getCityID() {
        return CityID;
    }

    public double getCashOfWallet() {
        return CashOfWallet;
    }

    public String getEmail() {
        return Email;
    }

    public double getTodayProfit() {
        return TodayProfit;
    }

    public String getBankName() {
        return BankName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLanguageID() {
        return LanguageID;
    }

    public String getLastName() {
        return LastName;
    }

    public String getCreditCard() {
        return CreditCard;
    }

    public String getMelliCode() {
        return MelliCode;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }

    public String getPresentorID() {
        return PresentorID;
    }

    public String getProfilePicURL() {
        return ProfilePicURL;
    }

    public double getPoint() {
        return Point;
    }

    public String getUserName() {
        return UserName;
    }

    public String getProvinceID() {
        return ProvinceID;
    }

    public String getUserTypeID() {
        return UserTypeID;
    }

    public boolean isUser() {
        return IsUser;
    }

    public void setUser(boolean user) {
        this.IsUser = user;
    }

    public void setAcquaintanceID(int acquaintanceID) {
        this.AcquaintanceID = acquaintanceID;
    }

    public void setCreditCard(String creditCard) {
        this.CreditCard = creditCard;
    }

    public void setAcquaintanceName(String acquaintanceName) {
        this.AcquaintanceName = acquaintanceName;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public void setCreditCardOwner(String creditCardOwner) {
        this.CreditCardOwner = creditCardOwner;
    }

    public void setFieldID(int fieldID) {
        this.FieldID = fieldID;
    }

    public void setFieldName(String fieldName) {
        this.FieldName = fieldName;
    }

    public void setPostalCode(String postalCode) {
        this.PostalCode = postalCode;
    }

    public void setSheba(String sheba) {
        this.Sheba = sheba;
    }

    public void setStoreName(String storeName) {
        this.StoreName = storeName;
    }

    public void setToken(String token) {
        this.Token = token;
    }

    public int getFieldID() {
        return FieldID;
    }

    public String getSheba() {
        return Sheba;
    }

    public int getAcquaintanceID() {
        return AcquaintanceID;
    }

    public String getAcquaintanceName() {
        return AcquaintanceName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public String getAddress() {
        return Address;
    }

    public String getCreditCardOwner() {
        return CreditCardOwner;
    }

    public String getFieldName() {
        return FieldName;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getToken() {
        return Token;
    }

    public String getRegisteryDate() {
        return RegisteryDate;
    }

    public double getIncome() {
        return Income;
    }

    public int getChanceCount() {
        return ChanceCount;
    }

    public int getUserCount() {
        return UserCount;
    }

    public String getPerenestorCode() {
        return perenestorCode;
    }

    public String getRegisteryDatePersian() {
        return RegisteryDatePersian;
    }

    public boolean isAgencyRequest() {
        return AgencyRequest;
    }

    public void setCityName(String cityName) {
        this.CityName = cityName;
    }

    public String getCityName() {
        return CityName;
    }

    public User(SoapObject input) {

        try {
            ID = input.getPropertySafelyAsString("ID");
            UserName = input.getPropertySafelyAsString("UserName");
            Password = input.getPropertySafelyAsString("Password");
            FirstName = input.getPropertySafelyAsString("FirstName");
            LastName = input.getPropertySafelyAsString("LastName");
            Email = input.getPropertySafelyAsString("Email");
            Phone = input.getPropertySafelyAsString("Phone");
            MelliCode = input.getPropertySafelyAsString("MelliCode");
            ProfilePicURL = input.getPropertySafelyAsString("ProfilePicURL");
            CityID = input.getPropertySafelyAsString("CityID");
            ProvinceID = input.getPropertySafelyAsString("ProvinceID");
            PresentorID = input.getPropertySafelyAsString("PresentorID");
            UserTypeID = input.getPropertySafelyAsString("UserTypeID");
            LanguageID = input.getPropertySafelyAsString("LanguageID");
            Point = Double.parseDouble(input.getPropertySafelyAsString("Point"));
            TodayProfit = Double.parseDouble(input.getPropertySafelyAsString("TodayProfit"));
            CashOfWallet = Double.parseDouble(input.getPropertySafelyAsString("CashOfWallet"));
            CreditCard = input.getPropertySafelyAsString("CreditCard");
            Sheba = input.getPropertySafelyAsString("Sheba");
            BankName = input.getPropertySafelyAsString("BankName");
            StoreName = input.getPropertySafelyAsString("StoreName");
            FieldID = Integer.parseInt(input.getPropertySafelyAsString("FieldID"));
            CreditCardOwner = input.getPropertySafelyAsString("CreditCardOwner");
            FieldName = input.getPropertySafelyAsString("FieldName");
            AcquaintanceID = Integer.parseInt(input.getPropertySafelyAsString("AcquaintanceID"));
            AcquaintanceName = input.getPropertySafelyAsString("AcquaintanceName");
            Address = input.getPropertySafelyAsString("Address");
            PostalCode = input.getPropertySafelyAsString("PostalCode");
            Token = input.getPropertySafelyAsString("Token");
            IsUser = Boolean.parseBoolean(input.getPropertySafelyAsString("IsUser"));
            UserCount = Integer.parseInt(input.getPropertySafelyAsString("UserCount"));
            perenestorCode = input.getPropertySafelyAsString("perenestorCode");
            AgencyRequest = Boolean.parseBoolean(input.getPropertySafelyAsString("AgencyRequest"));
            ChanceCount = Integer.parseInt(input.getPropertySafelyAsString("ChanceCount"));
            Income = Double.parseDouble(input.getPropertySafelyAsString("Income"));
            RegisteryDatePersian = input.getPropertySafelyAsString("RegisteryDatePersian");
            CityName = input.getPropertySafelyAsString("CityName");


            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                ExpirationDate = formatter.parse(input.getPropertySafelyAsString("ExpDate"));
            } catch (Exception ex) {
                ExpirationDate = new Date();
            }




        } catch (Exception e) {
            Log.e("User Soap", e.toString());
        }
    }
}
