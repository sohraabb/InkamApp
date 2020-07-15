package com.fara.inkamapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.BottomSheetFragments.UserProfile;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.jyn.vcview.VerificationCodeView;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class SmsVerification extends AppCompatActivity {

    private static final String LOG_TAG = "Agha_Sori";
    private VerificationCodeView smsCodeView;
    private TextView toastText, sendCode;
    private String phoneNumber, code;


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

        setContentView(R.layout.activity_sms_verification);

        smsCodeView = findViewById(R.id.et_sms_verification);
        sendCode = findViewById(R.id.btn_send_code_again);

        smsCodeView.setmEtWidth(100);

        Bundle intent = getIntent().getExtras();

        phoneNumber = intent.getString("phoneNumber");

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new sendCodeVerification().execute();
            }
        });


        smsCodeView.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {

            }

            @Override
            public void onComplete(View view, String content) {
                code = content;
                new checkVerificationCode().execute();
            }
        });
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                // message is the fetching OTP
                if (message.length() == 4) {
                    smsCodeView.onTextChanged(message,0,0,4);

//                    smsCodeView.setText(message);
//                    checkButton.performClick();
                }

//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            }
        }
    };

    private class checkVerificationCode extends AsyncTask<Void, Void, Boolean> {

        boolean results = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            results = new Caller().checkVerificationCode(phoneNumber, code);

            return results;
        }

        @Override
        protected void onPostExecute(Boolean isCorrect) {
            super.onPostExecute(isCorrect);
            //TODO we should add other items here too

            if (isCorrect != null) {
//                if (isCorrect) {
                    Intent intent = new Intent(getApplicationContext(), CompleteProfile.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                    finish();

//                }
//            else {
//                    Toast toast = Toast.makeText(getApplicationContext(), R.string.error_input, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toastText = toast.getView().findViewById(android.R.id.message);
//                    toast.getView().setBackgroundResource(R.drawable.toast_background);
//                    if (toastText != null) {
//                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
//                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
//                        toastText.setGravity(Gravity.CENTER);
//                        toastText.setTextSize(14);
//                    }
//                    toast.show();
//
//                }

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

    private class sendCodeVerification extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            results = new Caller().insertPhone(Numbers.ToEnglishNumbers(phoneNumber));

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus login) {
            super.onPostExecute(login);
            //TODO we should add other items here too

            if (login != null) {


                Toast toast = Toast.makeText(getApplicationContext(), R.string.code_sent, Toast.LENGTH_SHORT);
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

    //Initialization Varibles
    private void initViews() {
        if ((ActivityCompat.checkSelfPermission(SmsVerification.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(SmsVerification.this, new String[]{Manifest.permission.RECEIVE_SMS}, 100);
        } else {
            //Permission Granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                } else {
                    //Permission Not Granted
                    Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
