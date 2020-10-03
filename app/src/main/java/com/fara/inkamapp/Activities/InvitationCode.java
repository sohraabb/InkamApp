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
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.UserList;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class InvitationCode extends HideKeyboard {

    private Button submit;
    private EditText presenterCode;
    private TextView noCode, toastText;
    private String _firstName, _lastName, _cityID, _password, _phoneNumber, _username, _profilePicURL, _isUser, _encryptedPassword, _encryptedKey, _presenterCode, _token, _userID;
    private JSONObject postData;
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

        setContentView(R.layout.activity_invitation_code);
        initVariables();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    private void initVariables() {

        submit = findViewById(R.id.btn_submit_code);
        presenterCode = findViewById(R.id.et_presenter_code);
        noCode = findViewById(R.id.tv_do_not_have_code);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenterCode = presenterCode.getText().toString();
                new checkVerificationCode().execute();

            }
        });

        noCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new insertUser().execute();

            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            _username = intent.getString("UserName");
            _firstName = intent.getString("FirstName");
            _lastName = intent.getString("LastName");
            _cityID = intent.getString("CityID");
            _password = intent.getString("Password");
            _phoneNumber = intent.getString("Phone");
            _isUser = intent.getString("IsUser");
        }
    }

    private class checkVerificationCode extends AsyncTask<Void, Void, Boolean> {

        boolean results = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            results = new Caller().checkPresentageCode(_presenterCode);

            return results;
        }

        @Override
        protected void onPostExecute(Boolean isCorrect) {
            super.onPostExecute(isCorrect);
            //TODO we should add other items here too

            if (isCorrect != null) {
                new insertUser().execute();

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_presenter_code, Toast.LENGTH_SHORT);
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

    private class insertUser extends AsyncTask<Void, Void, UserList> {

        UserList results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            postData = new JSONObject();
            try {
                _encryptedPassword = Base64.encode((RSA.encrypt(_password, publicKey)));
                _encryptedKey = Base64.encode((RSA.encrypt(sharedpreferences.getString("key", null), publicKey)));

                postData.put("UserName", _username);
                postData.put("FirstName", _firstName);
                postData.put("LastName", _lastName);
                postData.put("CityID", _cityID);
                postData.put("Password", _encryptedPassword);
                postData.put("Phone", _phoneNumber);
                postData.put("ProfilePicURL", sharedpreferences.getString("ProfilePicURL", null));
                postData.put("IsUser", _isUser);
                postData.put("AesKey", _encryptedKey);
                postData.put("perenestorCode", _presenterCode);



            } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException |
                    InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected UserList doInBackground(Void... params) {
            String jsonToInsert = postData.toString().replace("\\n", "");
            String input = jsonToInsert.replace("\\", "");
            results = new Caller().insertUser(input);

            return results;
        }

        @Override
        protected void onPostExecute(UserList userList) {
            super.onPostExecute(userList);
            //TODO we should add other items here too

            if (userList != null && userList.get_status().equals("SUCCESS")) {
                try {
                    _token = AESEncyption.decryptMsg(userList.get_users().get(0).getToken(), sharedpreferences.getString("key", null));
                    _userID = userList.get_users().get(0).getID();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Token", _token);
                    editor.putString("UserID", _userID);
                    editor.putString("UserName", _username);
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
