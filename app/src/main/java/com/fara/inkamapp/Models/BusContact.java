package com.fara.inkamapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BusContact implements Parcelable {
    private String Name;
    private String MobilePhone;
    private String Email;

    public BusContact() {
    }

    public BusContact(String _name, String _mobilePhone, String _email) {
        this.Name = _name;
        this.MobilePhone = _mobilePhone;
        this.Email = _email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public BusContact(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Name = data[0];
        this.MobilePhone = data[1];
        this.Email = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.Name,
                this.MobilePhone,
                this.Email,});

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
