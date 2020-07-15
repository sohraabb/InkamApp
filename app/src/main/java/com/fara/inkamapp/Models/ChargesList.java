package com.fara.inkamapp.Models;

public class ChargesList {
    private int _logo;
    private String _amount;

    public void set_amount(String _amount) {
        this._amount = _amount;
    }

    public void set_logo(int _logo) {
        this._logo = _logo;
    }

    public int get_logo() {
        return _logo;
    }
    public String get_amount() {
        return _amount;
    }

    public ChargesList(int logo, String amount){
        this._amount = amount;
        this._logo = logo;
    }

}
