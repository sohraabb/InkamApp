package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Models.CheckUsername;
import com.fara.inkamapp.Models.Province;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;

public class LoginInkam extends HideKeyboard {

    private Button btn_continue;
    private EditText phoneNumber;
    private TextView toastText;
    private String phone;
    private String preCodeNumber = "0";
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjL8NCgYgt7Y0Lg9OZUaziHSPusQoVpuHIkICjy7YI8yUlRBETmtNr9wdu61Wskz0PAQbj/TnCSXOhnhbWDormPk0GWyTjV/4Drrlx+hZtxPDgrYSwqscqoG2HWmWVlaqbAuVz4r/XMDbcy8zPy/ROGVey4uyGKj0hsA4p3O6YMwIDAQAB";
    private SharedPreferences sharedPreferences;
    private checkUsername _checkUsername;
    private loginVerification _loginVerification;
    private ProgressBar loadingProgress;

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

        setContentView(R.layout.activity_login_inkam);
        initVariables();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
        phoneNumber.setText("");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (_checkUsername != null) {
            _checkUsername.cancel(true);
        }
        if (_loginVerification != null) {
            _loginVerification.cancel(true);
        }
    }

    private void initVariables() {
        phoneNumber = findViewById(R.id.et_phone_number);
        btn_continue = findViewById(R.id.btn_continue);
        loadingProgress = findViewById(R.id.progress_loader);

        phoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() < 10 || s.toString().trim().length() > 11) {
                    btn_continue.setEnabled(false);
                    btn_continue.setBackgroundResource(R.drawable.button_background_disabled);
                    btn_continue.setTextColor(getResources().getColor(R.color.colorNightRider));
                } else {
                    btn_continue.setEnabled(true);
                    btn_continue.setBackgroundResource(R.drawable.button_background_green);
                    btn_continue.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _checkUsername = new checkUsername();
                _checkUsername.execute();

            }
        });
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class checkUsername extends AsyncTask<Void, Void, CheckUsername> {

        CheckUsername results = null;

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
            } else {
                loadingProgress.setVisibility(View.VISIBLE);
                loadingProgress.setActivated(true);
            }

            // Sohrab : please check this with RegularExpression Later

            // !phone.matches("(\\+98|0)?9\\d{9}")

            if (phoneNumber.getText().toString().startsWith("0"))
                phone = phoneNumber.getText().toString();
            else if (!phoneNumber.getText().toString().startsWith("0"))
                phone = preCodeNumber + phoneNumber.getText().toString();
        }

        @Override
        protected CheckUsername doInBackground(Void... params) {
            results = new Caller().checkUsername(phone);

            return results;
        }

        @Override
        protected void onPostExecute(CheckUsername login) {
            super.onPostExecute(login);
            //TODO we should add other items here too

            if (login != null) {

                if (login.get_checkUserNameResult() == 0) {
                    _loginVerification = new loginVerification();
                    _loginVerification.execute();

                } else if (login.get_checkUserNameResult() > 0) {
                    Intent intent = new Intent(LoginInkam.this, LoginUsePassword.class);
                    intent.putExtra("phoneNumber", phone);
                    startActivity(intent);
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

            loadingProgress.setActivated(false);
            loadingProgress.setVisibility(View.GONE);
        }

    }

    private class loginVerification extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

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
            } else {
                loadingProgress.setVisibility(View.VISIBLE);
                loadingProgress.setActivated(true);
            }

        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            results = new Caller().insertPhone(phone);

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus login) {
            super.onPostExecute(login);
            //TODO we should add other items here too

            if (login != null && login.get_status().equals("SUCCESS")) {
                Intent intent = new Intent(LoginInkam.this, SmsVerification.class);
                intent.putExtra("phoneNumber", phone);
                startActivity(intent);
                finish();

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
            loadingProgress.setActivated(false);
            loadingProgress.setVisibility(View.GONE);
        }
    }


}
