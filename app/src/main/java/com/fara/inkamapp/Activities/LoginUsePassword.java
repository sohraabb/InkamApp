package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.rsa;
import com.fara.inkamapp.Models.CheckUsername;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginUsePassword extends AppCompatActivity {

    private Button submit;
    private TextView forgot_pass, toastText;
    private String phone, pass, token;
    private EditText password;

    private String publicKey = "";
    private String privateKey = "";
    private byte[] encodeData = null;


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

        setContentView(R.layout.activity_login_use_password);

        submit = findViewById(R.id.btn_enter);
        forgot_pass = findViewById(R.id.tv_forgot_pass);
        password = findViewById(R.id.et_password);

//        try {
//            Map<String, Object> keyMap = rsa.initKey();
//            publicKey = rsa.getPublicKey(keyMap);
//            privateKey = rsa.getPrivateKey(keyMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        Bundle intent = getIntent().getExtras();
        phone = intent.getString("phoneNumber");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrypt();
//                encrypt();

                new agentLogin().execute();
//                Intent intent = new Intent(getApplicationContext(), CompleteProfile.class);
//                startActivity(intent);
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoverPassword.class);
                startActivity(intent);
            }
        });

        new getUserById().execute();

    }

    public void encrypt() {
        byte[] rsaData = password.getText().toString().getBytes();

        try {
            encodeData = rsa.encryptByPublicKey(rsaData,"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");
            pass = new BigInteger(1, encodeData).toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decrypt(){
        try {
            String decodeData = rsa.decryptByPrivateKey("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");
            token = new String(decodeData);
            Log.i("RSA TOKEN", "Token : " + decodeData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
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
            }

        }

        @Override
        protected User doInBackground(Void... params) {
            String jsonToInsert = pass.toString().replace("\\n", "");
            String passData = jsonToInsert.replace("\\", "");
            results = new Caller().agentLogin(phone, passData);

            return results;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            //TODO we should add other items here too

            if (user != null) {
                decrypt();


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();

            }
        }
    }

    private class getUserById extends AsyncTask<Void, Void, User> {

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
            }

        }

        @Override
        protected User doInBackground(Void... params) {
            results = new Caller().getUserById("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            //TODO we should add other items here too

            if (user != null) {

            }
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
