package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> _users;
    private String _status;

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_users(ArrayList<User> _users) {
        this._users = _users;
    }

    public String get_status() {
        return _status;
    }

    public ArrayList<User> get_users() {
        return _users;
    }

    public UserList(SoapObject input) {

        try {
            _users = new ArrayList<>();

            //get list of passengers
            SoapObject userList = (SoapObject) input.getPropertySafely("UserList");
            if (userList != null) {
                for (int i = 0; i < userList.getPropertyCount(); ++i) {

                    _users.add(new User((SoapObject) userList.getProperty(i)));
                }
            }
            _status = input.getPropertySafelyAsString("Status");

//            SoapObject status = (SoapObject) input.getPropertySafely("Status");
//            _status = new Status(status);

        } catch (Exception e) {
            Log.e("User Soap", e.toString());
        }
    }
}
