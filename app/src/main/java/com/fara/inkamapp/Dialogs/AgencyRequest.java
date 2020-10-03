package com.fara.inkamapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Fragments.Users;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Helpers.RSAUtil;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.fara.inkamapp.Activities.MainActivity.MyPREFERENCES;
import static com.fara.inkamapp.Activities.MainActivity.publicKey;


public class AgencyRequest extends Dialog {

    private Activity activity;
    private ImageButton cancel;
    private TextView toastText;
    private EditText email, postalCode, cardNumber, shebaNum, bankName, bankOwnerName, storeName;
    private Button submit;
    private User userInfo;
    private SharedPreferences sharedPreferences;
    private JSONObject user;
    private String AesKey, _userId, _token;

    public AgencyRequest(Activity _activity, User _user) {
        super(_activity);
        // TODO Auto-generated constructor stub
        this.activity = _activity;
        this.userInfo = _user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_agency_request);
        cancel = findViewById(R.id.ib_cancel);
        email = findViewById(R.id.et_email_address);
        postalCode = findViewById(R.id.et_postal_code);
        cardNumber = findViewById(R.id.et_card_number);
        shebaNum = findViewById(R.id.et_sheba);
        bankName = findViewById(R.id.et_bank_name);
        bankOwnerName = findViewById(R.id.et_owner_name);
        storeName = findViewById(R.id.et_store_name);
        submit = findViewById(R.id.btn_submit);

        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        AesKey = sharedPreferences.getString("key", null);
        try {
            _userId = sharedPreferences.getString("UserID", null);
            _token = com.fara.inkamapp.Helpers.Base64.encode((RSA.encrypt(sharedPreferences.getString("Token", null), publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.length() > 0 && postalCode.length() > 0 && cardNumber.length() > 0 && shebaNum.length() > 0 && bankName.length() > 0
                        && bankOwnerName.length() > 0 && storeName.length() > 0) {
                    Toast.makeText(getContext(), "Good job", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Fill the empty boxes", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    private class UpdateUser extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            user = new JSONObject();
            try {
                userInfo.setFirstName(userInfo.getFirstName());
                userInfo.setLastName(userInfo.getLastName());
                userInfo.setCityID(userInfo.getCityID());
                userInfo.setEmail(email.getText().toString());
                userInfo.setPostalCode(postalCode.getText().toString());
                userInfo.setBankName(bankName.getText().toString());
                userInfo.setCreditCardOwner(bankOwnerName.getText().toString());
                userInfo.setStoreName(storeName.getText().toString());

                if (userInfo.getCreditCard() != null && !userInfo.getCreditCard().equals("anyType{}")) {
                    String card = AESEncyption.decryptMsg(userInfo.getCreditCard(), AesKey);
                    userInfo.setCreditCard(Base64.encode(RSA.encrypt(card, publicKey)));
                } else {
                    String card = AESEncyption.decryptMsg(cardNumber.getText().toString(), AesKey);
                    userInfo.setCreditCard(Base64.encode(RSA.encrypt(card, publicKey)));
                }
                if (userInfo.getSheba() != null && !userInfo.getSheba().equals("anyType{}")) {
                    String sheba = AESEncyption.decryptMsg(userInfo.getSheba(), AesKey);
                    userInfo.setSheba(Base64.encode(RSA.encrypt(sheba, publicKey)));
                } else {
                    String sheba = AESEncyption.decryptMsg(shebaNum.getText().toString(), AesKey);
                    userInfo.setSheba(Base64.encode(RSA.encrypt(sheba, publicKey)));
                }

                userInfo.setAgencyRequest(true);


//                if (userInfo.getRegisteryDate() != null && !userInfo.getSheba().equals(""))
//                    userInfo.setRegisteryDate("");
//
//                if (userInfo.getExpirationDate() != null && !userInfo.getSheba().equals(""))
//                    userInfo.setExpirationDate(new Date());
//
//                if (userInfo.getRegisteryDatePersian() != null && !userInfo.getSheba().equals(""))
//                    userInfo.setRegisteryDatePersian("");


//                userInfo.setUserName(sharedPreferences.getString("UserName", null));
//                userInfo.setID(sharedPreferences.getString("UserID", null));


//                user.put("ID", sharedPreferences.getString("UserID", null));
//                user.put("FirstName", firstName.getText().toString());
//                user.put("LastName", lastName.getText().toString());
//                user.put("UserName", sharedPreferences.getString("UserName", null));
//                user.put("ProfilePicURL", _encodeImage);
//                user.put("CityID", _cityID);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            Gson g = new Gson();
            String u = g.toJson(userInfo);
            results = new Caller().updateUser(_token, u);

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus responseStatus) {
            super.onPostExecute(responseStatus);
            //TODO we should add other items here too

            if (responseStatus != null && responseStatus.get_status().equals("SUCCESS")) {
                Toast toast = Toast.makeText(getContext(), R.string.user_success, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                dismiss();

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();

            } else {
                Toast toast = Toast.makeText(getContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }
}