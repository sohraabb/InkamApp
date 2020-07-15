package com.fara.inkamapp.Models;

import android.util.Log;

import com.fara.inkamapp.WebServices.Soap;

import org.ksoap2.serialization.SoapObject;

public class PhoneBillParameters {
    private PhoneBillFinalTermInfo _finalTermInfo;
    private PhoneBillMidTermInfo _midTermInfo;

    public void set_finalTermInfo(PhoneBillFinalTermInfo _finalTermInfo) {
        this._finalTermInfo = _finalTermInfo;
    }

    public void set_midTermInfo(PhoneBillMidTermInfo _midTermInfo) {
        this._midTermInfo = _midTermInfo;
    }

    public PhoneBillFinalTermInfo get_finalTermInfo() {
        return _finalTermInfo;
    }

    public PhoneBillMidTermInfo get_midTermInfo() {
        return _midTermInfo;
    }

    public PhoneBillParameters(SoapObject input) {
        try {

            Object midTermObj = input.getPropertySafely("MidTerm");
            if (midTermObj.toString() != null) {
                SoapObject midTerm = (SoapObject) midTermObj;
                _midTermInfo = new PhoneBillMidTermInfo(midTerm);

            }


            Object finalTermObj = input.getPropertySafely("FinalTerm");
            if (finalTermObj.toString() != null) {
                SoapObject finalTerm = (SoapObject) finalTermObj;
                _finalTermInfo = new PhoneBillFinalTermInfo(finalTerm);
            }

        } catch (Exception e) {
            Log.e("PhoneBill Soap", e.toString());
        }
    }
}
