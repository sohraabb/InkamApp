package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class Financial {
    private int Price;
    private float MaxApplicableDiscountPercentage;
    private List<String> AvailablePaymentMethods;
    private float CommissionPercentage;

    public Financial(SoapObject input) {
        try {
            AvailablePaymentMethods = new ArrayList<>();
            Price = Integer.parseInt(input.getPropertySafelyAsString("Price"));
            MaxApplicableDiscountPercentage = Float.valueOf(input.getPropertySafelyAsString("MaxApplicableDiscountPercentage"));
            CommissionPercentage = Float.valueOf(input.getPropertySafelyAsString("CommissionPercentage"));
            AvailablePaymentMethods.add(input.getPropertySafelyAsString("AvailablePaymentMethods"));
        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public float getMaxApplicableDiscountPercentage() {
        return MaxApplicableDiscountPercentage;
    }

    public void setMaxApplicableDiscountPercentage(float maxApplicableDiscountPercentage) {
        MaxApplicableDiscountPercentage = maxApplicableDiscountPercentage;
    }

    public List<String> getAvailablePaymentMethods() {
        return AvailablePaymentMethods;
    }

    public void setAvailablePaymentMethods(List<String> availablePaymentMethods) {
        AvailablePaymentMethods = availablePaymentMethods;
    }

    public float getCommissionPercentage() {
        return CommissionPercentage;
    }

    public void setCommissionPercentage(float commissionPercentage) {
        CommissionPercentage = commissionPercentage;
    }
}
