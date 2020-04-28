package com.fara.inkamapp.Models;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class PackageDataPlanListByPeriod {
    private String _responseCode;
    private ArrayList<PackagesDataPlan> _packagesDataPlans;
    private List<String> _responseMessage;


    public void set_responseCode(String _responseCode) {
        this._responseCode = _responseCode;
    }

    public void set_packagesDataPlans(ArrayList<PackagesDataPlan> _packagesDataPlans) {
        this._packagesDataPlans = _packagesDataPlans;
    }

    public ArrayList<PackagesDataPlan> get_packagesDataPlans() {
        return _packagesDataPlans;
    }

    public String get_responseCode() {
        return _responseCode;
    }

    public void set_responseMessage(List<String> _responseMessage) {
        this._responseMessage = _responseMessage;
    }

    public List<String> get_responseMessage() {
        return _responseMessage;
    }

    public PackageDataPlanListByPeriod(SoapObject input) {
        try {
            _packagesDataPlans = new ArrayList<>();
            _responseMessage = new ArrayList<>();

            _responseCode = input.getPropertySafelyAsString("ResponseCode");


            SoapObject responseMessages = (SoapObject) input.getPropertySafely("ResponseMessage");
            if (responseMessages != null) {
                for (int i = 0; i < responseMessages.getPropertyCount(); ++i) {

                    _responseMessage.add(String.valueOf(responseMessages.getProperty(i)));
                }
            }


            SoapObject dataPlanList = (SoapObject) input.getPropertySafely("DataPlanLists");
            if (dataPlanList != null) {
                for (int i = 0; i < dataPlanList.getPropertyCount(); ++i) {

                    _packagesDataPlans.add(new PackagesDataPlan((SoapObject) dataPlanList.getProperty(i)));
                }
            }

        } catch (Exception e) {
            Log.e("CheckCode Soap", e.toString());
        }
    }
}
