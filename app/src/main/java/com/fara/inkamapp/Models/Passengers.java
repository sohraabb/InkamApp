package com.fara.inkamapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

public class Passengers implements Parcelable {
    private String FirstName;
    private String LastName;
    private int SeatNumber;
    private String NationalCode;
    private String Gender;
    private String Mobile;
    private String Email;

    public Passengers() {

    }


    public Passengers(SoapObject input) {

        try {
            FirstName = input.getPropertySafelyAsString("FirstName");
            LastName = input.getPropertySafelyAsString("LastName");
            SeatNumber = Integer.parseInt(input.getPropertySafelyAsString("SeatNumber"));
            NationalCode = input.getPropertySafelyAsString("NationalCode");
            Gender = input.getPropertySafelyAsString("Gender");

        } catch (Exception e) {
            Log.e("ResponseStatus Soap", e.toString());
        }
    }


    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        SeatNumber = seatNumber;
    }

    public String getNationalCode() {
        return NationalCode;
    }

    public void setNationalCode(String nationalCode) {
        NationalCode = nationalCode;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Passengers(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.FirstName = data[0];
        this.LastName = data[1];
        this.SeatNumber = Integer.parseInt(data[2]);
        this.NationalCode = data[3];
        this.Gender = data[4];
        this.Mobile = data[5];
        this.Email = data[6];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.FirstName,
                this.LastName,
                this.NationalCode,
                this.Email, this.Mobile, this.Gender, String.valueOf(this.SeatNumber)});

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Passengers createFromParcel(Parcel in) {
            return new Passengers(in);
        }

        public Passengers[] newArray(int size) {
            return new Passengers[size];
        }
    };
}
