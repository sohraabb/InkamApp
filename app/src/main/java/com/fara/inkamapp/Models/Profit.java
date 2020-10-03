package com.fara.inkamapp.Models;

import org.ksoap2.serialization.SoapObject;

public class Profit {
    private int UserCount;
    private Double PanelProfit;
    private double userProfit;
    private Double WalletBalance;
    private String Message;

    public Profit(SoapObject input) {
        UserCount = Integer.parseInt(input.getPropertySafelyAsString("UserCount"));
        PanelProfit = Double.valueOf(input.getPropertySafelyAsString("PanelProfit"));
        userProfit = Double.parseDouble(input.getPropertySafelyAsString("userProfit"));
        WalletBalance = Double.valueOf(input.getPropertySafelyAsString("WalletBalance"));
        Message = input.getPropertySafelyAsString("Message");
    }

    public int getUserCount() {
        return UserCount;
    }

    public void setUserCount(int userCount) {
        UserCount = userCount;
    }

    public Double getPanelProfit() {
        return PanelProfit;
    }

    public void setPanelProfit(Double panelProfit) {
        PanelProfit = panelProfit;
    }

    public double getUserProfit() {
        return userProfit;
    }

    public void setUserProfit(double userProfit) {
        this.userProfit = userProfit;
    }

    public Double getWalletBalance() {
        return WalletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        WalletBalance = walletBalance;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
