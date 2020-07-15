package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.Data;
import com.fara.inkamapp.Models.PayInfo;
import com.fara.inkamapp.Models.PaymentResult;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.ServiceBillInfo;
import com.fara.inkamapp.Models.TrafficFines;
import com.fara.inkamapp.Models.UserWallet;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;
import com.top.lib.mpl.view.PaymentInitiator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class FinalPayment extends HideKeyboard {
    private String title, paymentId, billId, amount, plateNumber, address, pdfURL,
            currentDate, extraInfo, fullName, paymentDate, paymentID, previousDate,
            token, userID, encryptedToken, AesKey, dataToConfirm,location,city,datetime,type;
    private SharedPreferences sharedpreferences;
    private TextView serviceTitle, serviceAmount, serviceBillID, servicePaymentID, toastText;
    private Button payWallet, payOnline;
    private JSONObject postData;
    private int paymentType;
    private Bundle intent;
    private LinearLayout ll_billsAndCarFines, ll_netPackages, ll_charge, ll_phoneDebt, ll_single_bill, ll_bill_type;
    private PaymentResult paymentRes;
    private long orderID;
    private ImageButton back;


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
        payOnline = findViewById(R.id.btn_pay);
        ll_charge = findViewById(R.id.ll_single_row_charge_info);
        ll_netPackages = findViewById(R.id.ll_single_row_net_package_info);
        ll_single_bill = findViewById(R.id.ll_single_row_info);
        ll_phoneDebt = findViewById(R.id.ll_two_rows_info);
        ll_billsAndCarFines = findViewById(R.id.ll_three_rows_info);
        ll_bill_type = findViewById(R.id.ll_bill_type);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);

        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        new GetUserWallet().execute();

        intent = getIntent().getExtras();

        if (intent != null)
            paymentType = intent.getInt("paymentType", 0);
        NumberFormat formatter = new DecimalFormat("#,###");
        switch (paymentType) {
            case 0:
                ll_bill_type.setVisibility(View.VISIBLE);
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
                servicePaymentID.setText(Numbers.ToPersianNumbers(paymentID));
                serviceBillID.setText(Numbers.ToPersianNumbers(billId));

                String formattedNumber = formatter.format(Double.valueOf( amount));

                serviceAmount.setText(Numbers.ToPersianNumbers(formattedNumber));

                break;

            case 1:
                ll_bill_type.setVisibility(View.VISIBLE);
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
                    city = intent.getString("city");
                    location = intent.getString("location");
                    datetime = intent.getString("datetime");
                    type = intent.getString("type");
                }

                serviceTitle.setText(title);
                servicePaymentID.setText(Numbers.ToPersianNumbers(paymentID));
                serviceBillID.setText(Numbers.ToPersianNumbers(billId));
                serviceAmount.setText(Numbers.ToPersianNumbers(formatter.format(Double.valueOf(amount))));

                postData = new JSONObject();
                try {
                    postData.put("Amount", amount);
                    postData.put("BillID", billId);
                    postData.put("City", city);
                    postData.put("DateTime", datetime);
                    postData.put("Location", location);
                    postData.put("PaymentID", paymentId);
                    postData.put("Type", type);

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

        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentType == 0)

                    new GetBillToken().execute();
                else if (paymentType == 1)

                    new GetBillToken().execute();
                else if (paymentType == 2)
                    new GetBillToken().execute();
                else if (paymentType == 3)
                    new GetBillToken().execute();
                else if (paymentType == 4)
                    new GetBillToken().execute();

            }
        });
        payWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentType == 0)
                    new BillPaymentFromWallet().execute();
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

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
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

            results = new Caller().trafficBillPaymentFromWallet(postData.toString(), plateNumber, userID, encryptedToken);

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
    private class BillPaymentFromWallet extends AsyncTask<Void, Void, String> {

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

            results = new Caller().BillPaymentFromWallet(postData.toString(), userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too


            if (response !=null) {
                Intent intent = new Intent(getApplicationContext(), BuyResult.class);
               // intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
                intent.putExtra("type", "bill");
                // intent.putExtra("operator", operator);
                intent.putExtra("amount", amount);
               // intent.putExtra("date", reserveTopUpRequest.get_date());
                /// intent.putExtra("mobile", phone);
                //intent.putExtra("refrenceNumber", reserveTopUpRequest.get_referenceNumber());


                if (response.equals("تراکنش موفق")) {

                    intent.putExtra("success", true);


                } else {
                    intent.putExtra("success", false);


                }
                startActivity(intent);

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

            results = new Caller().topupServiceReserveFromWallet(userID, encryptedToken, postData.toString(), 0);

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

    private class GetUserWallet extends AsyncTask<Void, Void, UserWallet> {

        UserWallet results = null;

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
        protected UserWallet doInBackground(Void... params) {
            results = new Caller().getUserWallet(userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(UserWallet userWallet) {
            super.onPostExecute(userWallet);
            //TODO we should add other items here too


            if (userWallet != null) {

                if (userWallet.get_balance() < Double.valueOf(amount)) {
                    payWallet.setEnabled(false);
                } else {
                    payWallet.setEnabled(true);
                    payWallet.setText(R.string.pay);
                    payWallet.setBackgroundResource(R.drawable.btn_yellow_background);
                    payWallet.setTextColor(Color.WHITE);
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

    private class GetBillToken extends AsyncTask<Void, Void, PaymentResult> {

        PaymentResult results = null;

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
        protected PaymentResult doInBackground(Void... params) {


            String res = new Caller().GetBillToken(userID, encryptedToken, Long.valueOf("9375273701"), Long.valueOf(paymentId), Long.valueOf(billId));
            try {
                Gson gson = new Gson();
                results = gson.fromJson(AESEncyption.decryptMsg(res, AesKey), PaymentResult.class);


            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(PaymentResult serviceBillInfo) {
            super.onPostExecute(serviceBillInfo);
            //TODO we should add other items here too


            if (serviceBillInfo != null) {


                orderID = serviceBillInfo.getOrderID();
                Intent intent = new Intent(getApplicationContext(), PaymentInitiator.class);
                intent.putExtra("Type", "2");
                intent.putExtra("Token", serviceBillInfo.getData().getToken());

                startActivityForResult(intent, 1);


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

    private class BillSuccess extends AsyncTask<Void, Void, PayInfo> {

        PayInfo results = null;

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
        protected PayInfo doInBackground(Void... params) {
            try {
                String res = new Caller().BillSuccess(userID, encryptedToken, dataToConfirm, Base64.encode((RSA.encrypt(String.valueOf(orderID), publicKey))), Base64.encode((RSA.encrypt(amount, publicKey))));
         String json=  AESEncyption.decryptMsg(res,AesKey);
         Gson g= new Gson();
        results= g.fromJson(json, PayInfo.class);


            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(PayInfo data) {
            super.onPostExecute(data);
            //TODO we should add other items here too
            Intent intent = new Intent(getApplicationContext(), BuyResult.class);

            intent.putExtra("type", "bill");
            // intent.putExtra("operator", operator);
            intent.putExtra("amount", amount);
            /// intent.putExtra("mobile", phone);
            intent.putExtra("date", data.getPayDTime());
            intent.putExtra("refrenceNumber", data.getTrcNo());



            if (data.getStatus() == 0) {

                intent.putExtra("success", true);


            } else {
                intent.putExtra("success", false);

            }
            startActivity(intent);

        }
    }
    private class TrafficBillSuccess extends AsyncTask<Void, Void, PayInfo> {

        PayInfo results = null;

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
        protected PayInfo doInBackground(Void... params) {
            try {
                String res = new Caller().TrafficBillSuccess( postData.toString(),userID, encryptedToken,  Base64.encode((RSA.encrypt(String.valueOf(orderID), publicKey))), Base64.encode((RSA.encrypt(amount, publicKey))),dataToConfirm);
                String json=  AESEncyption.decryptMsg(res,AesKey);
                Gson g= new Gson();
                results= g.fromJson(json, PayInfo.class);


            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(PayInfo data) {
            super.onPostExecute(data);
            //TODO we should add other items here too
            Intent intent = new Intent(getApplicationContext(), BuyResult.class);

            intent.putExtra("type", "bill");
            // intent.putExtra("operator", operator);
            intent.putExtra("amount", amount);
            /// intent.putExtra("mobile", phone);
            intent.putExtra("date", data.getPayDTime());
            intent.putExtra("refrenceNumber", data.getTrcNo());



            if (data.getStatus() == 0) {

                intent.putExtra("success", true);


            } else {
                intent.putExtra("success", false);

            }
            startActivity(intent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        dataToConfirm = data.getStringExtra("enData");
        if (resultCode == 3) {
            dataToConfirm = data.getStringExtra("enData");
            if (!dataToConfirm.equals(null))
                dataToConfirm = dataToConfirm.replace("\\", "");
            String one = data.getStringExtra("message");
            String sts = String.valueOf(data.getIntExtra("status", 0));
            if (sts.equals("0")) {
                if(paymentType==0) {
                    new BillSuccess().execute();
                }
                else if(paymentType==1){
                    new TrafficBillSuccess().execute();
                }
            }
        } else if (resultCode == 4) {
            Toast toast = Toast.makeText(getApplicationContext(), "خطا" +
                    "در" +
                    "ثبت" +
                    "درخواست" +
                    "پرداخت" +
                    "قبض", Toast.LENGTH_SHORT);
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
        } else if (resultCode == 6) {
            Toast toast = Toast.makeText(getApplicationContext(), "خطای" +
                    "داخلی" +
                    "کتابخانه" +
                    "در" +
                    "ثبت" +
                    "پرداخت" +
                    "قبض", Toast.LENGTH_SHORT);
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

        // }
    }//

}
