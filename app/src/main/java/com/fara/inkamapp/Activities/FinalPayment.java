package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.TrafficFines;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class FinalPayment extends AppCompatActivity {
    private String title, paymentId, billId, amount, plateNumber, address, pdfURL, currentDate, extraInfo, fullName, paymentDate, paymentID, previousDate;
    private TextView serviceTitle, serviceAmount, serviceBillID, servicePaymentID, toastText;
    private Button payWallet;
    private JSONObject postData;
    private int paymentType;
    private Bundle intent;
    private LinearLayout ll_billsAndCarFines, ll_netPackages, ll_charge, ll_phoneDebt, ll_single_bill;


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

        setContentView(R.layout.activity_final_payment);

        serviceTitle = findViewById(R.id.tv_service_title);
        servicePaymentID = findViewById(R.id.tv_payment_id);
        serviceBillID = findViewById(R.id.tv_bills_id);
        serviceAmount = findViewById(R.id.tv_price);
        payWallet = findViewById(R.id.btn_pay_wallet);

        ll_charge = findViewById(R.id.ll_single_row_charge_info);
        ll_netPackages = findViewById(R.id.ll_single_row_net_package_info);
        ll_single_bill = findViewById(R.id.ll_single_row_info);
        ll_phoneDebt = findViewById(R.id.ll_two_rows_info);
        ll_billsAndCarFines = findViewById(R.id.ll_three_rows_info);


        intent = getIntent().getExtras();
        if (intent != null)
            paymentType = intent.getInt("paymentType", 0);

        switch (paymentType) {
            case 0:
                ll_billsAndCarFines.setVisibility(View.VISIBLE);
                ll_charge.setVisibility(View.GONE);
                ll_phoneDebt.setVisibility(View.GONE);
                ll_netPackages.setVisibility(View.GONE);

                if (intent != null) {
                    title = intent.getString("title");
                    address = intent.getString("address");
                    amount = intent.getString("amount");
                    billId = intent.getString("billID");
                    pdfURL = intent.getString("pdfURL");
                    currentDate = intent.getString("currentDate");
                    extraInfo = intent.getString("extraInfo");
                    fullName = intent.getString("fullName");
                    paymentDate = intent.getString("paymentDate");
                    paymentId = intent.getString("paymentID");
                    previousDate = intent.getString("previousDate");

                }

                serviceTitle.setText(title);
                servicePaymentID.setText(paymentId);
                serviceBillID.setText(billId);
                serviceAmount.setText(amount);

                break;

            case 1:
                ll_billsAndCarFines.setVisibility(View.VISIBLE);
                ll_charge.setVisibility(View.GONE);
                ll_phoneDebt.setVisibility(View.GONE);
                ll_netPackages.setVisibility(View.GONE);

                if (intent != null) {
                    title = intent.getString("title");
                    billId = intent.getString("billID");
                    paymentId = intent.getString("paymentID");
                    amount = intent.getString("amount");
                    plateNumber = intent.getString("plateNumber");
                }

                serviceTitle.setText(title);
                servicePaymentID.setText(paymentId);
                serviceBillID.setText(billId);
                serviceAmount.setText(amount);

                postData = new JSONObject();
                try {
                    postData.put("Amount", "300000");
                    postData.put("BillID", "6664815800297");
                    postData.put("City", "تهران");
                    postData.put("DateTime", "03/09/2020 15:15:00");
                    postData.put("Delivery", "الصاقی");
                    postData.put("Delivery", "حقاني");
                    postData.put("PaymentID", "0000030036399");
                    postData.put("Type", "هرنوع توقف که منجر به سد معبر و یا اختلا");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 2:
                ll_charge.setVisibility(View.VISIBLE);
                ll_billsAndCarFines.setVisibility(View.GONE);
                ll_phoneDebt.setVisibility(View.GONE);
                ll_netPackages.setVisibility(View.GONE);

                break;

            case 3:
                ll_netPackages.setVisibility(View.VISIBLE);
                ll_charge.setVisibility(View.GONE);
                ll_billsAndCarFines.setVisibility(View.GONE);
                ll_phoneDebt.setVisibility(View.GONE);

                break;

            case 4:

                ll_phoneDebt.setVisibility(View.VISIBLE);
                ll_netPackages.setVisibility(View.VISIBLE);
                ll_charge.setVisibility(View.GONE);
                ll_billsAndCarFines.setVisibility(View.GONE);
                ll_single_bill.setVisibility(View.GONE);

                break;

            case 5:
                ll_single_bill.setVisibility(View.VISIBLE);
                ll_phoneDebt.setVisibility(View.VISIBLE);
                ll_netPackages.setVisibility(View.VISIBLE);
                ll_charge.setVisibility(View.GONE);
                ll_billsAndCarFines.setVisibility(View.GONE);
                break;


            default:
                break;
        }


        payWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentType == 0)
                    new TopupServiceReserveFromWallet().execute();
                else if (paymentType == 1)
                    new TrafficBillPaymentFromWallet().execute();
                else if (paymentType == 2)
                    new TrafficBillPaymentFromWallet().execute();
                else if (paymentType == 3)
                    new TrafficBillPaymentFromWallet().execute();
                else if (paymentType == 4)
                    new TrafficBillPaymentFromWallet().execute();

            }
        });
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class TrafficBillPaymentFromWallet extends AsyncTask<Void, Void, String> {

        String results = null;

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
        protected String doInBackground(Void... params) {

            results = new Caller().trafficBillPaymentFromWallet(postData.toString(), plateNumber, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too


            if (response != null) {


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

    private class TopupServiceReserveFromWallet extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

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
        protected ReserveTopupRequest doInBackground(Void... params) {

            results = new Caller().topupServiceReserveFromWallet("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", postData.toString(), 0);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopupRequest) {
            super.onPostExecute(reserveTopupRequest);
            //TODO we should add other items here too


            if (reserveTopupRequest != null) {


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
