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
import android.widget.Toast;

import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.JavaSource_Calendar;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.TokenResponse;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    //    private String currentDate, expirationDate;
    private GregorianCalendar currentDate, expirationDate;
    private boolean isExpired = true;
    private String _userID, _token, _encryptedToken;

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

        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.getString("UserID", null) != null && sharedPreferences.getString("Token", null) != null) {
            _userID = sharedPreferences.getString("UserID", null);
            try {
                _token = Base64.encode((RSA.encrypt(sharedPreferences.getString("Token", null), publicKey)));
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        new CheckUserToken().execute();


    }

    private class CheckUserToken extends AsyncTask<Void, Void, TokenResponse> {

        TokenResponse results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected TokenResponse doInBackground(Void... params) {
            results = new Caller().checkToken(_userID, _token);

            return results;
        }

        @Override
        protected void onPostExecute(final TokenResponse tokenResponse) {
            super.onPostExecute(tokenResponse);
            //TODO we should add other items here too

            if (tokenResponse != null) {

                Thread myThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                            if (sharedPreferences.getBoolean("my_first_time", true)) {
                                //the app is being launched for first time, do something
                                Intent intent = new Intent(getApplicationContext(), IntroSliders.class);
                                startActivity(intent);
                                finish();

                            } else {

                                if (tokenResponse.is_isValid()) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), LoginInkam.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                myThread.start();

            }

        }

    }

}
