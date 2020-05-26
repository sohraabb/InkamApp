package com.fara.inkamapp.Helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class FourDigitCardFormatWatcher implements TextWatcher {

    // Change this to what you want... ' ', '-' etc..
    private final String space = "  ";
    EditText et_filed;


    public FourDigitCardFormatWatcher(EditText et_filed){
        this.et_filed = et_filed;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String initial = s.toString();
        // remove all non-digits characters
        String processed = initial.replaceAll("\\D", "");

        if (processed.length() > 4){
            processed = processed.replaceAll("(\\d{4})(?=\\d)", "$1 ");
        }

        //Remove the listener
        et_filed.removeTextChangedListener(this);

        //Assign processed text
        et_filed.setText(processed);

        try {
            et_filed.setSelection(processed.length());
        } catch (Exception e) {
            // TODO: handle exception
        }

        //Give back the listener
        et_filed.addTextChangedListener(this);
    }
}
