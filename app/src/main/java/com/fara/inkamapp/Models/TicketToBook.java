package com.fara.inkamapp.Models;

import java.util.List;

public class TicketToBook {
    private String BusID ;

    private float DesiredDiscountPercentage ;
    private List<Passengers> Passengers ;
    private BusContact Contact ;
    private String DestinationCityCode ;
    private String OriginCityCode ;
    private String DepartureDate ;
    private double Amount ;
    private String PhoneNumber ;
    private String HookUrl ;
    public TicketToBook(){

    }
    public TicketToBook(String busID, float desiredDiscountPercentage, List<Passengers> passengers, BusContact contact, String destinationCityCode, String originCityCode, String departureDate, double amount, String phoneNumber) {
        BusID = busID;
        DesiredDiscountPercentage = desiredDiscountPercentage;
        Passengers = passengers;
        Contact = contact;
        DestinationCityCode = destinationCityCode;
        OriginCityCode = originCityCode;
        DepartureDate = departureDate;
        Amount = amount;
        PhoneNumber = phoneNumber;
        HookUrl="inkam.ir";
    }


    public float getDesiredDiscountPercentage() {
        return DesiredDiscountPercentage;
    }

    public void setDesiredDiscountPercentage(float desiredDiscountPercentage) {
        DesiredDiscountPercentage = desiredDiscountPercentage;
    }

    public List<Passengers> getPassengers() {
        return Passengers;
    }

    public void setPassengers(List<Passengers> passengers) {
        Passengers = passengers;
    }

    public BusContact getContact() {
        return Contact;
    }

    public void setContact(BusContact contact) {
        Contact = contact;
    }



    public String getDestinationCityCode() {
        return DestinationCityCode;
    }

    public void setDestinationCityCode(String destinationCityCode) {
        DestinationCityCode = destinationCityCode;
    }

    public String getOriginCityCode() {
        return OriginCityCode;
    }

    public void setOriginCityCode(String originCityCode) {
        OriginCityCode = originCityCode;
    }

    public String getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(String departureDate) {
        DepartureDate = departureDate;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getBusID() {
        return BusID;
    }

    public void setBusID(String busID) {
        BusID = busID;
    }

    public String getHookUrl() {
        return HookUrl;
    }

    public void setHookUrl(String hookUrl) {
        HookUrl = hookUrl;
    }
}
