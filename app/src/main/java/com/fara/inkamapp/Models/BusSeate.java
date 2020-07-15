package com.fara.inkamapp.Models;

import com.fara.inkamapp.WebServices.Soap;

import org.ksoap2.serialization.SoapObject;

public class BusSeate {
    private String Number ;
    private String Column ;
    private String Row ;
    private String Status ;

    public BusSeate(SoapObject input) {
        Number = input.getPropertySafelyAsString("Number");;
        Column = input.getPropertySafelyAsString("Column");;
        Row = input.getPropertySafelyAsString("Row");;
        Status = input.getPropertySafelyAsString("Status");;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getColumn() {
        return Column;
    }

    public void setColumn(String column) {
        Column = column;
    }

    public String getRow() {
        return Row;
    }

    public void setRow(String row) {
        Row = row;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
