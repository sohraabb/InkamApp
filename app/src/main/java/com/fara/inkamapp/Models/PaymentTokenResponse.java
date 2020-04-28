package com.fara.inkamapp.Models;

import java.util.Date;

public class PaymentTokenResponse {
    public String Token ;
    public Date ExpireDate ;

    public void setExpireDate(Date expireDate) {
        ExpireDate = expireDate;
    }

    public void setToken(String token) {
        Token = token;
    }

    public Date getExpireDate() {
        return ExpireDate;
    }

    public String getToken() {
        return Token;
    }
}
