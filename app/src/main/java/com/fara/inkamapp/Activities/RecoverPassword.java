package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class RecoverPassword extends AppCompatActivity {

    private EditText et_code, et_password;
    private Button submit;
    private String code, newPassword;
    private TextView toastText;

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

        et_code = findViewById(R.id.et_code);
        et_password = findViewById(R.id.et_new_password);
        submit = findViewById(R.id.btn_accept);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = et_code.getText().toString();
                newPassword = et_password.getText().toString();
                new updatePassword().execute();
            }
        });

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
            results = new Caller().insertPassword(MainActivity._userId, MainActivity._token, code, newPassword);

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus login) {
            super.onPostExecute(login);
            //TODO we should add other items here too

            if (login != null && login.get_status().equals("SUCCESS")) {
                Intent intent = new Intent(RecoverPassword.this, MainActivity.class);
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
        }

    }

}
