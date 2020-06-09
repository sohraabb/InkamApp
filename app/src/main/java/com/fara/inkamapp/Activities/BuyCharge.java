package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class BuyCharge extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rightel_back, irancell_back, hamrah_back, normalCharge_back, amazingCharge_back;
    private TextView toastText, amazingCharge_text, normalCharge_text;
    private JSONObject postData;
    private ReserveTopupRequest reserveTopUpRequest;
    private EditText phoneNumber;
    private RecyclerView chargeList;

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

        setContentView(R.layout.activity_buy_charge);

        phoneNumber = findViewById(R.id.et_enter_phone_number);
        rightel_back = findViewById(R.id.rl_rightel);
        hamrah_back = findViewById(R.id.rl_hamrah_aval);
        irancell_back = findViewById(R.id.rl_irancell);
        chargeList = findViewById(R.id.rv_charges);
        normalCharge_back = findViewById(R.id.rl_normal_charge);
        amazingCharge_back = findViewById(R.id.rl_amazing_charge);
        normalCharge_text = findViewById(R.id.tv_normal_charge);
        amazingCharge_text = findViewById(R.id.tv_amazing_charge);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isIrancell(s.toString())) {
                    irancell_back.setBackgroundResource(R.drawable.green_stroke_background);

                } else if (isHamrahAval(s.toString())) {
                    hamrah_back.setBackgroundResource(R.drawable.green_stroke_background);

                } else if (isRightel(s.toString())) {
                    rightel_back.setBackgroundResource(R.drawable.green_stroke_background);
                }

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

        new GetChargeType().execute();
        reserveTopUpRequest = new ReserveTopupRequest();

//        charge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                postData = new JSONObject();
//                try {
//                    postData.put("Amount", "10000");
//                    postData.put("CellNumbers", "09374227117");
//                    postData.put("CellNumber", "9374227117");
//                    postData.put("ChargeType", "0");
//                    postData.put("BankId", "08");
//                    postData.put("DeviceType", "59");
//                    postData.put("Operator", "0");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//

                new TopUpRequest().execute();

//            }
//        });
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

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_amazing_charge:
                amazingCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
                amazingCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));

                normalCharge_back.setBackgroundResource(R.drawable.gray_rounded_background);
                normalCharge_text.setTextColor(getResources().getColor(R.color.colorBrown));


                break;
            case R.id.rl_normal_charge:
                normalCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
                normalCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));

                amazingCharge_back.setBackgroundResource(R.drawable.gray_rounded_background);
                amazingCharge_text.setTextColor(getResources().getColor(R.color.colorBrown));


                break;

            default:
                break;
        }
    }


    private class GetChargeType extends AsyncTask<Void, Void, String> {

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
            results = new Caller().getChargeType(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(String chargeType) {
            super.onPostExecute(chargeType);
            //TODO we should add other items here too


            if (!chargeType.equals("anyType")) {


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

    private class TopUpRequest extends AsyncTask<Void, Void, ReserveTopupRequest> {

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
            results = new Caller().topupServiceReserve("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", postData.toString(), 0);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            if (reserveTopUpRequest.get_token() != null) {

                Intent intent = new Intent(getApplicationContext(), BuyCharge2.class);
                intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
                intent.putExtra("data", "");
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
}
