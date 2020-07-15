package com.fara.inkamapp.Activities;

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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
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

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;
import static com.fara.inkamapp.Helpers.AESEncyption.sohrabGeneratesAESKey;

public class LoginUsePassword extends HideKeyboard implements View.OnClickListener {

    private Button submit;
    private TextView forgot_pass, toastText;
    private String phone, _passwords, token, encryptedPassword, AesKey, encryptedKey;
    private EditText password;
    private SharedPreferences sharedpreferences;
    private ImageButton back;
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

        setContentView(R.layout.activity_login_use_password);
        initVariables();


    }

    private void initVariables() {
        submit = findViewById(R.id.btn_enter);
        forgot_pass = findViewById(R.id.tv_forgot_pass);
        password = findViewById(R.id.et_password);
        back = findViewById(R.id.ib_back);
        loadingProgress = findViewById(R.id.progress_loader);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle intent = getIntent().getExtras();
        phone = intent.getString("phoneNumber");

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter:
                _passwords = password.getText().toString();
                try {
                    encryptedPassword = Base64.encode((RSA.encrypt(_passwords, publicKey)));
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


                break;
            case R.id.tv_forgot_pass:
//                new SendTextMessage().execute();
                Intent intent = new Intent(getApplicationContext(), RecoverPassword.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                break;

            case R.id.ib_back:
                onBackPressed();
                finish();
                break;
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
            results = new Caller().getUserById(MainActivity._userId, MainActivity._token);

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


    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }
}
