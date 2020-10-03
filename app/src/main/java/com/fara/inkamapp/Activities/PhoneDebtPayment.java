package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.PayInfo;
import com.fara.inkamapp.Models.PaymentResult;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.UserWallet;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;
import com.top.lib.mpl.view.PaymentInitiator;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class PhoneDebtPayment extends HideKeyboard {

    private TextView tvPhone, tvTermTitle, tvTermAmount, toastText;
    private String _phone, _termTitle, _termAmount, _billId, _paymentId, userID, encryptedToken, AesKey, dataToConfirm, token, userName;
    private SharedPreferences sharedpreferences;
    private Button payWallet, payOnline;
    private long orderID;

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

        setContentView(R.layout.activity_phone_debt_payment);

        initVariables();
        new GetUserWallet().execute();

    }

    private void initVariables() {
        tvPhone = findViewById(R.id.tv_phone_number);
        tvTermTitle = findViewById(R.id.tv_term_title);
        tvTermAmount = findViewById(R.id.tv_term_value);
        payWallet = findViewById(R.id.btn_pay_wallet);
        payOnline = findViewById(R.id.btn_pay);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            _phone = intent.getString("phoneNumber");
            _termTitle = intent.getString("termTitle");
            _termAmount = Numbers.ToEnglishNumbers(intent.getString("billAmount"));
            _billId = intent.getString("billID");
            _paymentId = intent.getString("paymentID");

            tvPhone.setText(_phone);
            tvTermTitle.setText(_termTitle);
            tvTermAmount.setText(Numbers.ToPersianNumbers(_termAmount));
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        userName = sharedpreferences.getString("UserName", null);

        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetBillToken().execute();

            }
        });

        payWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BillPaymentFromWallet().execute();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dataToConfirm = data.getStringExtra("enData");
        if (resultCode == 3) {
            dataToConfirm = data.getStringExtra("enData");
            if (dataToConfirm != null)
                dataToConfirm = dataToConfirm.replace("\\", "");
            String one = data.getStringExtra("message");
            String sts = String.valueOf(data.getIntExtra("status", 0));
            if (sts.equals("0")) {
                new BillSuccess().execute();
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

                if (userWallet.get_balance() < Double.parseDouble(_termAmount)) {
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


            String res = new Caller().GetBillToken(userID, encryptedToken, Long.parseLong(userName.substring(1)), Long.parseLong(_paymentId), Long.parseLong(_billId));
            try {
                Gson gson = new Gson();
                results = gson.fromJson(AESEncyption.decryptMsg(res, AesKey), PaymentResult.class);

            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException | InvalidAlgorithmParameterException |
                    InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
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
                String res = new Caller().BillSuccess(userID, encryptedToken, dataToConfirm, Base64.encode((RSA.encrypt(String.valueOf(orderID), publicKey))), Base64.encode((RSA.encrypt(_termAmount, publicKey))));
                String json = AESEncyption.decryptMsg(res, AesKey);
                Gson g = new Gson();
                results = g.fromJson(json, PayInfo.class);


            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException |
                    NoSuchAlgorithmException | UnsupportedEncodingException |
                    InvalidAlgorithmParameterException | InvalidParameterSpecException e) {

                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(PayInfo data) {
            super.onPostExecute(data);
            //TODO we should add other items here too
            Intent intent = new Intent(getApplicationContext(), BuyResult.class);

            intent.putExtra("type", "phone");
            // intent.putExtra("operator", operator);
            intent.putExtra("amount", _termAmount);
            /// intent.putExtra("mobile", phone);

            intent.putExtra("date", data.getPayDTime());
            intent.putExtra("refrenceNumber", String.valueOf(data.getTrcNo()));


            if (data.getStatus() == 0)
                intent.putExtra("success", true);
            else
                intent.putExtra("success", false);

            startActivity(intent);

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

            JSONObject postData = new JSONObject();
            try {
                postData.put("Amount", _termAmount);
                postData.put("BillID", _billId);
                postData.put("PayId", _paymentId);
            } catch (Exception e) {

            }
            results = new Caller().BillPaymentFromWallet(postData.toString(), userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too


            if (response != null) {
                Intent intent = new Intent(getApplicationContext(), BuyResult.class);

                intent.putExtra("type", "bill");

                intent.putExtra("amount", _termAmount);

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

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    public void backClicke(View v) {
        finish();
    }


}
