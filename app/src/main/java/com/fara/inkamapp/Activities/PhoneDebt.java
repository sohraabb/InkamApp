package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Dialogs.InquiryDebt;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.PhoneBillInfo;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class PhoneDebt extends HideKeyboard implements View.OnClickListener {

    private RelativeLayout mobileView, telephoneView;
    private TextView mobileText, telephoneText, instanceText, toastText;
    private EditText mobile, telephone;
    private Button inquiry;
    private Activity _context;
    private String phoneNumber, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId;
    private boolean isMobile;
    private String amount, operator, type, phone, token, userID, encryptedToken, AesKey, dataToConfirm;
    private SharedPreferences sharedpreferences;

    private static final int PICK_CONTACT = 856;


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

        setContentView(R.layout.activity_phone_debt);

        mobileView = findViewById(R.id.rl_mobile);
        telephoneView = findViewById(R.id.rl_telephone);
        mobileText = findViewById(R.id.tv_mobile);
        telephoneText = findViewById(R.id.tv_telephone);
        instanceText = findViewById(R.id.tv_phone_instance);
        mobile = findViewById(R.id.et_enter_phone_number);
        telephone = findViewById(R.id.et_enter_telephone_number);
        inquiry = findViewById(R.id.btn_inquiry);

        mobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() - 118 <= (mobile.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        }else {
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent, PICK_CONTACT);
                        }
                        return false;
                    }
                }
                return false;
            }
        });
        inquiry.setOnClickListener(this);
        mobileView.setOnClickListener(this);
        telephoneView.setOnClickListener(this);
        instanceText.setText("۳۲۳۲۳۴۵ ۰۳۴");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);

        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    private boolean isIrancell(String input) {
        Pattern p = Pattern.compile("^09[0|3][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private boolean isRightel(String input) {
        Pattern p = Pattern.compile("^09[2][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private boolean isHamrahAval(String input) {
        Pattern p = Pattern.compile("^09[1][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_mobile:
                mobileView.setBackgroundResource(R.drawable.green_rounded_background);
                mobileText.setTextColor(getResources().getColor(R.color.colorWhite));
                telephoneView.setBackgroundResource(R.drawable.light_rounded_background);
                telephoneText.setTextColor(getResources().getColor(R.color.colorBrown));
                instanceText.setText("۰۹۱۲۳۴۵۶۷۸۹");

                mobile.setVisibility(View.VISIBLE);
                telephone.setVisibility(View.INVISIBLE);
                isMobile = true;

                break;

            case R.id.rl_telephone:

                telephoneView.setBackgroundResource(R.drawable.green_rounded_background);
                telephoneText.setTextColor(getResources().getColor(R.color.colorWhite));
                mobileView.setBackgroundResource(R.drawable.light_rounded_background);
                mobileText.setTextColor(getResources().getColor(R.color.colorBrown));
                instanceText.setText("۳۲۳۲۳۴۵ ۰۳۴");

                telephone.setVisibility(View.VISIBLE);
                mobile.setVisibility(View.INVISIBLE);
                isMobile = false;

                break;

            case R.id.btn_inquiry:

                if (isMobile) {
                    phoneNumber = mobile.getText().toString();
                    if (isHamrahAval(phoneNumber))
                        new GetHamrahAvalBillInfo().execute();

                    else if (isRightel(phoneNumber))
                        new GetRightelBillInfo().execute();

                    else if (isIrancell(phoneNumber))
                        new GetIrancelBillInfo().execute();

                    else
                        Toast.makeText(getApplicationContext(), "Wrong Number", Toast.LENGTH_SHORT).show();


                } else {
                    phoneNumber = telephone.getText().toString();
                    new GetTelecomBillInfo().execute();
                }

        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class GetIrancelBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

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
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getIrancelBillInfo(phoneNumber, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }

                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
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

    private class GetHamrahAvalBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

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
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getHamrahAvalBillInfo(phoneNumber, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null && phoneBillInfo.get_status() != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }

                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
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

    private class GetRightelBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

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
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getRightelBillInfo(phoneNumber, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }

                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
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

    private class GetTelecomBillInfo extends AsyncTask<Void, Void, PhoneBillInfo> {

        PhoneBillInfo results = null;

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
        protected PhoneBillInfo doInBackground(Void... params) {
            results = new Caller().getTelecomBillInfo(phoneNumber, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(PhoneBillInfo phoneBillInfo) {
            super.onPostExecute(phoneBillInfo);
            //TODO we should add other items here too


            if (phoneBillInfo != null && phoneBillInfo.get_status().get_code() != null) {
                if (phoneBillInfo.get_status().get_code().equals("G00000")) {
                    if (phoneBillInfo.get_phoneBillParameters().get_midTermInfo() != null) {
                        midTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_amount());
                        midTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_billId());
                        midTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_midTermInfo().get_paymentId());
                    }
                    if (phoneBillInfo.get_phoneBillParameters().get_finalTermInfo() != null) {
                        finalTermAmount = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_amount());
                        finalTermBillId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_billId());
                        finalTermPaymentId = String.valueOf(phoneBillInfo.get_phoneBillParameters().get_finalTermInfo().get_paymentId());
                    }


                    InquiryDebt inquiryDebt = new InquiryDebt(PhoneDebt.this, midTermAmount, midTermBillId, midTermPaymentId, finalTermAmount, finalTermBillId, finalTermPaymentId, phoneNumber);
                    inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    inquiryDebt.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), phoneBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
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

    public void backClicke(View v) {
        finish();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    try {


                            Uri contactData = data.getData();
                            Cursor c = managedQuery(contactData, null, null, null, null);
                            if (c.moveToFirst()) {
                                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                                String hasPhone =
                                        c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                                if (hasPhone.equalsIgnoreCase("1")) {
                                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                    phones.moveToFirst();
                                    String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    mobile.setText(cNumber);
                                    // Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
                                    // setCn(cNumber);
                                }
                            }

                    } catch (Exception e) {
                        Toast.makeText(_context, e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
        }
    }
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContactNames() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[]          {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null,  null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if(people.moveToFirst()) {
            do {
                String name   = people.getString(indexName);
                String number = people.getString(indexNumber);
                // add number to list
                // Do work...
            } while (people.moveToNext());
        }
        people.close();
    }
}
