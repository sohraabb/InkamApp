package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class LoginUsePassword extends AppCompatActivity {

    private Button submit;
    private TextView forgot_pass, toastText;
    private String phone, _passwords, token, encryptedPassword, AesKey;
    private EditText password;
    private SharedPreferences sharedpreferences;


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

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle intent = getIntent().getExtras();
        AesKey = sharedpreferences.getString("key", null);
        phone = intent.getString("phoneNumber");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _passwords = password.getText().toString();
                try {
                    encryptedPassword = Base64.encode((RSA.encrypt(_passwords, publicKey)));
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
                new agentLogin().execute();

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
            String jsonToInsert = encryptedPassword.replace("\\n", "");
            String passData = jsonToInsert.replace("\\", "");
            results = new Caller().agentLogin(phone, passData, AesKey);

            return results;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            //TODO we should add other items here too

            if (user != null) {
                try {
                    token = AESEncyption.decryptMsg(user.get_token(), AesKey);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    finish();
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
}
