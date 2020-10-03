package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class BusSummary {
    private String ID;
    private String Company;
    private OperatingCompany OperatingCompany;
    private BoardingPoint BoardingPoint;
    private List<BoardingPoint> DroppingPoints;

    private String Type;
    private String Price;
    private Financial Financial;
    private String DepartureDate;
    private String Status;
    private String Description;
    private String AvailableSeats;

    public BusSummary(SoapObject input) {
        try {
            ID = input.getPropertySafelyAsString("ID");
            Company = input.getPropertySafelyAsString("Company");
            Type = input.getPropertySafelyAsString("Type");
            Price = input.getPropertySafelyAsString("Price");
            DepartureDate = input.getPropertySafelyAsString("DepartureDate");
            Status = input.getPropertySafelyAsString("Status");
            AvailableSeats = input.getPropertySafelyAsString("AvailableSeats");
            Description = input.getPropertySafelyAsString("Description");
            OperatingCompany = new OperatingCompany((SoapObject) input.getProperty("OperatingCompany"));
            BoardingPoint = new BoardingPoint((SoapObject) input.getProperty("BoardingPoint"));
            Financial = new Financial((SoapObject) input.getProperty("Financial"));
            SoapObject dr = (SoapObject) input.getProperty("DroppingPoints");
            DroppingPoints = new ArrayList<>();
            for (int i = 0; i < dr.getPropertyCount(); i++) {
                DroppingPoints.add(new BoardingPoint((SoapObject) dr.getProperty(i)));

            }

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public OperatingCompany getOperatingCompany() {
        return OperatingCompany;
    }

    public void setOperatingCompany(OperatingCompany operatingCompany) {
        OperatingCompany = operatingCompany;
    }

    public BoardingPoint getBoardingPoint() {
        return BoardingPoint;
    }

    public void setBoardingPoint(BoardingPoint boardingPoint) {
        BoardingPoint = boardingPoint;
    }

    public List<BoardingPoint> getDroppingPoints() {
        return DroppingPoints;
    }

    public void setDroppingPoints(List<BoardingPoint> droppingPoints) {
        DroppingPoints = droppingPoints;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Financial getFinancial() {
        return Financial;
    }

    public void setFinancial(Financial financial) {
        Financial = financial;
    }

    public String getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(String departureDate) {
        DepartureDate = departureDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAvailableSeats() {
        return AvailableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        AvailableSeats = availableSeats;
    }
}
