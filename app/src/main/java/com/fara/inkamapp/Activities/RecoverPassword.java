package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import static com.fara.inkamapp.Helpers.AESEncyption.sohrabGeneratesAESKey;


public class RecoverPassword extends HideKeyboard {

    private EditText et_code, et_password;
    private Button submit;
    private String code, newPassword, token, encryptedPassword, AesKey, encryptedKey;
    private TextView toastText, timer;
    private String userID, phone;
    private SharedPreferences sharedpreferences;
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

        setContentView(R.layout.activity_recover_password);
        initVariables();

        new SendTextMessage().execute();
    }

    private void initVariables() {
        et_code = findViewById(R.id.et_code);
        et_password = findViewById(R.id.et_new_password);
        submit = findViewById(R.id.btn_accept);
        timer = findViewById(R.id.tv_send_code_again);
        loadingProgress = findViewById(R.id.progress_loader);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userID = sharedpreferences.getString("UserID", null);
        Bundle intent = getIntent().getExtras();
        phone = intent.getString("phone");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = et_code.getText().toString();
                newPassword = et_password.getText().toString();
                new updatePassword().execute();
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTextMessage().execute();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class updatePassword extends AsyncTask<Void, Void, ResponseStatus> {

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
            }

        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            try {
                results = new Caller().insertPassword(userID, Base64.encode(RSA.encrypt(newPassword, publicKey)), Base64.encode(RSA.encrypt(code, publicKey)), phone);
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                    NoSuchPaddingException | NoSuchAlgorithmException e) {

                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus login) {
            super.onPostExecute(login);
            //TODO we should add other items here too

            if (login != null && login.get_status().equals("SUCCESS")) {

                try {
                    encryptedPassword = Base64.encode((RSA.encrypt(newPassword, publicKey)));
                    if (sharedpreferences.getString("key", null) != null) {
                        AesKey = sharedpreferences.getString("key", null);
                        encryptedKey = Base64.encode((RSA.encrypt(sharedpreferences.getString("key", null), publicKey)));

                    } else {
                        byte[] key1 = sohrabGeneratesAESKey(16);
                        AesKey = Base64.encode(key1);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("key", AesKey);
                        editor.apply();
                        encryptedKey = Base64.encode((RSA.encrypt(sharedpreferences.getString("key", null), publicKey)));

                    }

                } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                new agentLogin().execute();

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

    private class agentLogin extends AsyncTask<Void, Void, User> {

        User results = null;

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
        protected User doInBackground(Void... params) {
            String jsonToInsert = encryptedPassword.replace("\\n", "");
            String passData = jsonToInsert.replace("\\", "");
            results = new Caller().agentLogin(phone, passData, encryptedKey);

            return results;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            //TODO we should add other items here too

            if (user != null) {
                try {
                    if (sharedpreferences.getString("UserID", null) == null && user.getID() != null) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("UserID", user.getID());
                        editor.apply();
                    }

                    if (user.getToken() != null) {
                        token = AESEncyption.decryptMsg(user.getToken(), AesKey);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Token", token);
                        editor.apply();
                    }
                    if (user.getUserName() != null) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("UserName", user.getUserName());
                        editor.apply();
                    }
                    if (user.getExpirationDate() != null) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("expDate", user.getExpirationDate().toString());
                        editor.apply();
                    }

                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException |
                        InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

            loadingProgress.setActivated(false);
            loadingProgress.setVisibility(View.GONE);
        }
    }

    private class SendTextMessage extends AsyncTask<Void, Void, ResponseStatus> {

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
            }

            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timer.setText(Numbers.ToPersianNumbers(String.valueOf(millisUntilFinished / 1000)));
                    timer.setEnabled(false);
                }

                public void onFinish() {
                    timer.setText(R.string.send_code);
                    timer.setEnabled(true);

                }

            }.start();

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


            } else {
                Toast toast = Toast.makeText(getApplicationContext(), login.get_message(), Toast.LENGTH_SHORT);
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
