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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ApproveInternetPackage;
import com.fara.inkamapp.Models.Data;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.UserWallet;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.top.lib.mpl.view.PaymentInitiator;

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

public class BuyPackages extends HideKeyboard {
    private TextView toastText;
    private String amount, operator, type, phone, token, userID, encryptedToken, AesKey, dataToConfirm, orderId, describe, refrenceNumber;
    private SharedPreferences sharedpreferences;
    private Button payWallet;
    private JSONObject postData;
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

        setContentView(R.layout.activity_buy_packages);
        initVariables();

    }

    private void initVariables() {
        payWallet = findViewById(R.id.btn_pay_wallet);
        Button btnPay = findViewById(R.id.btn_pay);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvDescribe = findViewById(R.id.tv_package_content);
        ImageView ivLogo = findViewById(R.id.iv_logo);

        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);

        amount = getIntent().getStringExtra("amount");
        operator = getIntent().getStringExtra("operator");
        type = getIntent().getStringExtra("type");
        phone = getIntent().getStringExtra("phone");
        describe = getIntent().getStringExtra("describe");
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(Double.valueOf(amount));
        tvPrice.setText(Numbers.ToPersianNumbers(formattedNumber));
        tvDescribe.setText(Numbers.ToPersianNumbers(describe));

        switch (operator) {
            case "0":
                ivLogo.setImageResource(R.drawable.irancell_logo_green);
                break;
            case "1":
                ivLogo.setImageResource(R.drawable.hamrah_aval_logo_green);
                break;
            case "2":
                ivLogo.setImageResource(R.drawable.rightel_logo);
                break;
        }

        postData = new JSONObject();
        try {
            postData.put("Amount", amount);
            postData.put("CellNumbers", Base64.encode(RSA.encrypt(phone, publicKey)));
            postData.put("CellNumber", phone);
            postData.put("ChargeType", type);
            postData.put("BankId", "08");
            postData.put("DeviceType", "59");
            postData.put("Operator", operator);

        } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InternetPackageReserve().execute();
            }
        });
        payWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TopUpRequestForWallet().execute();
            }
        });

        new GetUserWallet().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            dataToConfirm = data.getStringExtra("enData");
            if (dataToConfirm != null)
                dataToConfirm = dataToConfirm.replace("\\", "");
            String one = data.getStringExtra("message");
            String sts = String.valueOf(data.getIntExtra("status", 0));
            if (sts.equals("0")) {
                new approveTopUpRequest().execute();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
        // }
    }//

    private class InternetPackageReserve extends AsyncTask<Void, Void, ReserveTopupRequest> {

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
                    toastText.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }


        @Override
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().internetPackageReserve(userID, encryptedToken, postData.toString());

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too

            if (reserveTopUpRequest.get_token() != null) {
                orderId = String.valueOf(reserveTopUpRequest.get_reserveNumber());
                Intent intent = new Intent(getApplicationContext(), PaymentInitiator.class);
                intent.putExtra("Type", "1");
                try {
                    intent.putExtra("Token", AESEncyption.decryptMsg(reserveTopUpRequest.get_token(), AesKey));
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException |
                        InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                intent.putExtra("OrderID", orderId);
                intent.putExtra("TSPEnabled", 1);
                startActivityForResult(intent, 1);

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/IRANSansMobile.ttf"));
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

                if (userWallet.get_balance() < Double.parseDouble(amount)) {
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

    private class TopUpRequestForWallet extends AsyncTask<Void, Void, ReserveTopupRequest> {

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
            results = new Caller().topupServiceReserveFromWallet(userID, encryptedToken, postData.toString(), 1);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            if (reserveTopUpRequest != null) {
                Intent intent = new Intent(getApplicationContext(), BuyResult.class);
                intent.putExtra("type", "internet");
                intent.putExtra("operator", operator);
                intent.putExtra("amount", amount);
                intent.putExtra("describe", describe);
                intent.putExtra("mobile", phone);
                intent.putExtra("date", reserveTopUpRequest.get_date());
                intent.putExtra("refrenceNumber", refrenceNumber);

                if (reserveTopUpRequest.get_referenceNumber() > 0 && reserveTopUpRequest.get_responseCode() == 0) {
                    intent.putExtra("success", true);

                } else {
                    intent.putExtra("success", false);
                }
                startActivity(intent);
            }
        }
    }

    private class approveTopUpRequest extends AsyncTask<Void, Void, ApproveInternetPackage> {

        ApproveInternetPackage results = null;

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
        protected ApproveInternetPackage doInBackground(Void... params) {
            try {
                results = new Caller().InternetPackageApprove(userID, encryptedToken, dataToConfirm, Base64.encode((RSA.encrypt(orderId, publicKey))), Base64.encode((RSA.encrypt(amount, publicKey))));
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                    NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(ApproveInternetPackage reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too
            if (reserveTopUpRequest != null && reserveTopUpRequest.get_data() != null) {
                Intent intent = new Intent(getApplicationContext(), BuyResult.class);

                intent.putExtra("type", "internet");
                intent.putExtra("operator", operator);
                intent.putExtra("amount", amount);
                intent.putExtra("describe", describe);
                intent.putExtra("mobile", phone);
                intent.putExtra("date", reserveTopUpRequest.get_dateTime());
                intent.putExtra("refrenceNumber", refrenceNumber);

                if (reserveTopUpRequest.get_status() == 0)
                    intent.putExtra("success", true);
                else
                    intent.putExtra("success", false);

                startActivity(intent);
            }
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }


}
