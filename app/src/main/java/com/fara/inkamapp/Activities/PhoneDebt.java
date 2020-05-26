package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Dialogs.InquiryDebt;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.PhoneBillInfo;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class PhoneDebt extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mobileView, telephoneView;
    private TextView mobileText, telephoneText, instanceText, toastText;
    private EditText mobile, telephone;
    private Button inquiry;
    private Activity _context;
    private String phoneNumber, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId;
    private boolean isMobile;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        setContentView(R.layout.activity_phone_debt);

        mobileView = findViewById(R.id.rl_mobile);
        telephoneView = findViewById(R.id.rl_telephone);
        mobileText = findViewById(R.id.tv_mobile);
        telephoneText = findViewById(R.id.tv_telephone);
        instanceText = findViewById(R.id.tv_phone_instance);
        mobile = findViewById(R.id.et_enter_phone_number);
        telephone = findViewById(R.id.et_enter_telephone_number);
        inquiry = findViewById(R.id.btn_inquiry);

        inquiry.setOnClickListener(this);
        mobileView.setOnClickListener(this);
        telephoneView.setOnClickListener(this);
        instanceText.setText("۳۲۳۲۳۴۵ ۰۳۴");

    }


    private boolean isIrancell(String input) {
        Pattern p = Pattern.compile("^09[0|3][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private boolean isRightel(String input) {
        Pattern p = Pattern.compile("^09[2][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private boolean isHamrahAval(String input) {
        Pattern p = Pattern.compile("^09[1][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_mobile:
                mobileView.setBackgroundResource(R.drawable.green_rounded_background);
                mobileText.setTextColor(getResources().getColor(R.color.colorWhite));
                telephoneView.setBackgroundResource(R.drawable.light_rounded_background);
                telephoneText.setTextColor(getResources().getColor(R.color.colorBrown));
                instanceText.setText("۰۹۱۲۳۴۵۶۷۸۹");

                mobile.setVisibility(View.VISIBLE);
                telephone.setVisibility(View.INVISIBLE);
                isMobile = true;

                break;

            case R.id.rl_telephone:

                telephoneView.setBackgroundResource(R.drawable.green_rounded_background);
                telephoneText.setTextColor(getResources().getColor(R.color.colorWhite));
                mobileView.setBackgroundResource(R.drawable.light_rounded_background);
                mobileText.setTextColor(getResources().getColor(R.color.colorBrown));
                instanceText.setText("۳۲۳۲۳۴۵ ۰۳۴");

                telephone.setVisibility(View.VISIBLE);
                mobile.setVisibility(View.INVISIBLE);
                isMobile = false;

                break;

            case R.id.btn_inquiry:

                if (isMobile) {
                    phoneNumber = mobile.getText().toString();
                    if (isHamrahAval(phoneNumber))
                        new GetHamrahAvalBillInfo().execute();

                    else if (isRightel(phoneNumber))
                        new GetRightelBillInfo().execute();

                    else if (isIrancell(phoneNumber))
                        new GetIrancelBillInfo().execute();

                    else
                        Toast.makeText(getApplicationContext(), "Wrong Number", Toast.LENGTH_SHORT).show();


                } else {
                    phoneNumber = telephone.getText().toString();
                    new GetTelecomBillInfo().execute();
                }

        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class GetIrancelBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getIrancelBillInfo(phoneNumber, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }

                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }


            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class GetHamrahAvalBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getHamrahAvalBillInfo(phoneNumber, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }

                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }


            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class GetRightelBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getRightelBillInfo(phoneNumber, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }

                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }


            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class GetTelecomBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getTelecomBillInfo(phoneNumber, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }


                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }


            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }
}
