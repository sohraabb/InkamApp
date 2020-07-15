package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class SpecificTicketInfo {

    private String _id;
    private String _busId;
    private ContactInfoModel _contactInfoModel;
    private String _ticketNumber;
    private String _company;
    private BoardingAndDroppingPoint _boardingPoint, _droppingPoint;
    // this is dateTime
    private String _departureDate;
    private String _busType;
    private int _price;
    private int _discount;
    private ArrayList<Passengers> _passengers;
    private String _status;
    private String _clientTraceNumber;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public void set_boardingPoint(BoardingAndDroppingPoint _boardingPoint) {
        this._boardingPoint = _boardingPoint;
    }

    public void set_busId(String _busId) {
        this._busId = _busId;
    }

    public void set_busType(String _busType) {
        this._busType = _busType;
    }

    public void set_clientTraceNumber(String _clientTraceNumber) {
        this._clientTraceNumber = _clientTraceNumber;
    }

    public void set_company(String _company) {
        this._company = _company;
    }

    public void set_contactInfoModel(ContactInfoModel _contactInfoModel) {
        this._contactInfoModel = _contactInfoModel;
    }

    public void set_departureDate(String _departureDate) {
        this._departureDate = _departureDate;
    }

    public void set_discount(int _discount) {
        this._discount = _discount;
    }

    public void set_droppingPoint(BoardingAndDroppingPoint _droppingPoint) {
        this._droppingPoint = _droppingPoint;
    }

    public void set_passengers(ArrayList<Passengers> _passengers) {
        this._passengers = _passengers;
    }

    public void set_price(int _price) {
        this._price = _price;
    }

    public void set_ticketNumber(String _ticketNumber) {
        this._ticketNumber = _ticketNumber;
    }

    public String get_id() {
        return _id;
    }

    public String get_status() {
        return _status;
    }

    public ArrayList<Passengers> get_passengers() {
        return _passengers;
    }

    public BoardingAndDroppingPoint get_boardingPoint() {
        return _boardingPoint;
    }

    public BoardingAndDroppingPoint get_droppingPoint() {
        return _droppingPoint;
    }

    public ContactInfoModel get_contactInfoModel() {
        return _contactInfoModel;
    }

    public int get_discount() {
        return _discount;
    }

    public int get_price() {
        return _price;
    }

    public String get_busId() {
        return _busId;
    }

    public String get_busType() {
        return _busType;
    }

    public String get_clientTraceNumber() {
        return _clientTraceNumber;
    }

    public String get_company() {
        return _company;
    }

    public String get_departureDate() {
        return _departureDate;
    }

    public String get_ticketNumber() {
        return _ticketNumber;
    }

    public SpecificTicketInfo(SoapObject input) {

        try {
            _passengers = new ArrayList<>();

            _id = input.getPropertySafelyAsString("ID");
            _busId = input.getPropertySafelyAsString("BusID");

            SoapObject contact = (SoapObject) input.getPropertySafely("Contact");
            _contactInfoModel = new ContactInfoModel(contact);

            _ticketNumber = input.getPropertySafelyAsString("TicketNumber");
            _company = input.getPropertySafelyAsString("Company");

            SoapObject boardingPoint = (SoapObject) input.getPropertySafely("BoardingPoint");
            _boardingPoint = new BoardingAndDroppingPoint(boardingPoint);

            SoapObject droppingPoint = (SoapObject) input.getPropertySafely("DroppingPoint");
            _droppingPoint = new BoardingAndDroppingPoint(droppingPoint);

            // this should be date
            _departureDate = input.getPropertySafelyAsString("DepartureDate");
            _busType = input.getPropertySafelyAsString("BusType");
            _price = Integer.parseInt(input.getPropertySafelyAsString("Price"));
            _discount = Integer.parseInt(input.getPropertySafelyAsString("Discount"));

            //get list of passengers
            SoapObject passengers = (SoapObject) input.getPropertySafely("Passengers");
            if (passengers != null) {
                for (int i = 0; i < passengers.getPropertyCount(); ++i) {

                    _passengers.add(new Passengers((SoapObject) passengers.getProperty(i)));
                }
            }
            _status = input.getPropertySafelyAsString("Status");
            _clientTraceNumber = input.getPropertySafelyAsString("ClientTraceNumber");


        } catch (Exception e) {
            Log.e("ResponseStatus Soap", e.toString());
        }
    }
}
