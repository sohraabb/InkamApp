package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.Models.UserList;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONException;
import org.json.JSONObject;

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

public class CompleteProfile extends AppCompatActivity {

    private Button submit;
    private TextView toastText;
    private EditText firstName, lastName, city, password;
    private JSONObject postData;

    private byte[] encodeData = null;
    private String phoneNumber;
    private String privateKey = "";


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

        setContentView(R.layout.activity_complete_profile);

        submit = findViewById(R.id.btn_submit_info);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        city = findViewById(R.id.et_your_city);
        password = findViewById(R.id.et_password);

        Bundle intent = getIntent().getExtras();

        phoneNumber = intent.getString("phone");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new insertUser().execute();

//                encrypt();

//                Intent intent = new Intent(getApplicationContext(), InvitationCode.class);
//                startActivity(intent);
            }
        });
    }

    private class insertUser extends AsyncTask<Void, Void, UserList> {

        UserList results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            postData = new JSONObject();
            try {
//                String encryptedPass = RSA.encrypt(password.getText().toString());

                postData.put("UserName", phoneNumber);
                postData.put("FirstName", firstName.getText().toString());
                postData.put("LastName", lastName.getText().toString());
                postData.put("CityID", "1D3284CA-6711-403B-9B46-470B9756DA10");
//                postData.put("Password", encryptedPass);
                postData.put("Phone", phoneNumber);
                postData.put("UserTypeID", "C78201B2-F9A4-45AA-9C38-890A526468AF");
                postData.put("ProfilePicURL", "");
                postData.put("IsUser", "true");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected UserList doInBackground(Void... params) {
            String jsonToInsert = postData.toString().replace("\\n","");
            String input = jsonToInsert.replace("\\","");
            results = new Caller().insertUser(input);

            return results;
        }

        @Override
        protected void onPostExecute(UserList userList) {
            super.onPostExecute(userList);
            //TODO we should add other items here too

            if (userList != null && userList.get_status().equals("SUCCESS")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userID", userList.get_users().get(0).get_id());
                intent.putExtra("token", userList.get_users().get(0).get_token());

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
