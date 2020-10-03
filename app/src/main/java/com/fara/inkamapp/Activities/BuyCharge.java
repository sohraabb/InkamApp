package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.ChargeAmountAdapter;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.BottomSheetFragments.UserProfile;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ChargesList;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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


public class BuyCharge extends HideKeyboard implements View.OnClickListener, ChargeAmountAdapter.ItemClickListener {

    private RelativeLayout rightel_back, irancell_back, hamrah_back, normalCharge_back, amazingCharge_back, creditCharge, permanentCharge;
    private LinearLayout ll_operators;
    private TextView toastText, amazingCharge_text, normalCharge_text, creditChargeText, permanentChargeText, purchased;
    private JSONObject postData;
    private EditText phoneNumber, preferredAmount;
    private RecyclerView chargeList;
    private ChargeAmountAdapter chargeAdapter;
    private SharedPreferences sharedPreferences;
    private String token, userID, encryptedToken;
    private String operator, type;
    private ImageButton back;
    private Button accept;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PICK_CONTACT = 856;
    private int previousOperatorState = 0;
    private int serviceType, chargeKind;

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

        setContentView(R.layout.activity_buy_charge);
        initVariables();

    }

    @Override
    public void onItemClick(View view, int position) {

        try {
            if (phoneNumber.getText().toString().length() < 11) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_phone_number, Toast.LENGTH_SHORT);
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
                Bundle bundle = new Bundle();
                bundle.putString("amount", Numbers.ToEnglishNumbers(chargeAdapter.getItem(position).get_amount()).replace(",", ""));
                bundle.putString("operator", operator);
                bundle.putString("type", type);
                bundle.putString("phone", Numbers.ToEnglishNumbers(phoneNumber.getText().toString().replace(" ", "")));
                bundle.putInt("serviceType", 0);
                bundle.putInt("chargeKind", chargeKind);

                bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }

        } catch (Exception e) {
            e.toString();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rl_irancell:
                rightel_back.setBackgroundResource(R.drawable.white_rounded_background);
                hamrah_back.setBackgroundResource(R.drawable.white_rounded_background);
                irancell_back.setBackgroundResource(R.drawable.green_stroke_background);

                break;

            case R.id.rl_hamrah_aval:
                rightel_back.setBackgroundResource(R.drawable.white_rounded_background);
                irancell_back.setBackgroundResource(R.drawable.white_rounded_background);
                hamrah_back.setBackgroundResource(R.drawable.green_stroke_background);


                break;

            case R.id.rl_rightel:
                rightel_back.setBackgroundResource(R.drawable.green_stroke_background);
                hamrah_back.setBackgroundResource(R.drawable.white_rounded_background);
                irancell_back.setBackgroundResource(R.drawable.white_rounded_background);

                break;


            case R.id.rl_amazing_charge:
                amazingCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
                amazingCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));

                normalCharge_back.setBackgroundResource(R.drawable.gray_rounded_background);
                normalCharge_text.setTextColor(getResources().getColor(R.color.colorBrown));
                type = "1";

                break;
            case R.id.rl_normal_charge:
                normalCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
                normalCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));

                amazingCharge_back.setBackgroundResource(R.drawable.gray_rounded_background);
                amazingCharge_text.setTextColor(getResources().getColor(R.color.colorBrown));
                type = "0";

                break;

            case R.id.btn_accept:
                if (phoneNumber.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.fill_phone_number, Toast.LENGTH_SHORT);
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

                } else if (phoneNumber.getText().toString().length() < 11) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.wrong_phone_number, Toast.LENGTH_SHORT);
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

                } else if (preferredAmount.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.fill_preferred_amount, Toast.LENGTH_SHORT);
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
                    Bundle bundle = new Bundle();
                    bundle.putString("amount", Numbers.ToEnglishNumbers(preferredAmount.getText().toString().replace(",", "")));
                    bundle.putString("operator", operator);
                    bundle.putString("type", type);
                    bundle.putString("phone", Numbers.ToEnglishNumbers(phoneNumber.getText().toString().replace(" ", "")));
                    bundle.putInt("chargeKind", chargeKind);
                    bundle.putInt("serviceType", 0);

                    bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                    bottomSheetDialogFragment.setArguments(bundle);
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }


                break;

            case R.id.rl_type_credit:
                creditCharge.setBackgroundResource(R.drawable.green_rounded_background);
                creditChargeText.setTextColor(getResources().getColor(R.color.colorWhite));

                permanentCharge.setBackgroundResource(R.drawable.gray_rounded_background);
                permanentChargeText.setTextColor(getResources().getColor(R.color.colorBrown));
                chargeKind = 0;

                ll_operators.setVisibility(View.VISIBLE);
                chargeList.setVisibility(View.VISIBLE);

                break;

            case R.id.rl_type_permanent:
                permanentCharge.setBackgroundResource(R.drawable.green_rounded_background);
                permanentChargeText.setTextColor(getResources().getColor(R.color.colorWhite));

                creditCharge.setBackgroundResource(R.drawable.gray_rounded_background);
                creditChargeText.setTextColor(getResources().getColor(R.color.colorBrown));
                ll_operators.setVisibility(View.INVISIBLE);
                chargeList.setVisibility(View.GONE);

                chargeKind = 2;
                break;

            case R.id.tv_purchased:
                Intent intent = new Intent(getApplicationContext(), PurchasedItems.class);
                intent.putExtra("purchasedType", 3);
                startActivity(intent);
                break;


            default:
                break;
        }
//        previousOperatorState = temp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);

            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == PICK_CONTACT) {
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
                            phoneNumber.setText(Numbers.ToPersianNumbers(cNumber.replace(" ", "").replace("+98", "0")));

                            phones.close();

                        }
                    }

                } catch (Exception e) {
                    Log.i("Charge Income", e.toString());
                }
            }
        }
    }

    private void initVariables() {
        phoneNumber = findViewById(R.id.et_enter_phone_number);
        rightel_back = findViewById(R.id.rl_rightel);
        hamrah_back = findViewById(R.id.rl_hamrah_aval);
        irancell_back = findViewById(R.id.rl_irancell);
        chargeList = findViewById(R.id.rv_charges);
        normalCharge_back = findViewById(R.id.rl_normal_charge);
        amazingCharge_back = findViewById(R.id.rl_amazing_charge);
        normalCharge_text = findViewById(R.id.tv_normal_charge);
        amazingCharge_text = findViewById(R.id.tv_amazing_charge);
        preferredAmount = findViewById(R.id.et_amount);
        accept = findViewById(R.id.btn_accept);
        back = findViewById(R.id.ib_back);
        creditCharge = findViewById(R.id.rl_type_credit);
        permanentCharge = findViewById(R.id.rl_type_permanent);
        creditChargeText = findViewById(R.id.tv_credit);
        permanentChargeText = findViewById(R.id.tv_permanent);
        purchased = findViewById(R.id.tv_purchased);
        ll_operators = findViewById(R.id.ll_operator);


        rightel_back.setOnClickListener(this);
        irancell_back.setOnClickListener(this);
        hamrah_back.setOnClickListener(this);
        purchased.setOnClickListener(this);
        accept.setOnClickListener(this);

        back.setOnClickListener(view -> onBackPressed());

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", null);
        userID = sharedPreferences.getString("UserID", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        normalCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
        normalCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));
        creditCharge.setBackgroundResource(R.drawable.green_rounded_background);
        creditChargeText.setTextColor(getResources().getColor(R.color.colorWhite));

        type = "0";
        chargeKind = 0;

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isIrancell(Numbers.ToEnglishNumbers(s.toString()))) {
                    irancell_back.setBackgroundResource(R.drawable.green_stroke_background);
                    hamrah_back.setBackgroundResource(R.drawable.white_rounded_background);
                    rightel_back.setBackgroundResource(R.drawable.white_rounded_background);
                    operator = "0";

                    initIrancellChargeList();


                } else if (isHamrahAval(Numbers.ToEnglishNumbers(s.toString()))) {
                    hamrah_back.setBackgroundResource(R.drawable.green_stroke_background);
                    irancell_back.setBackgroundResource(R.drawable.white_rounded_background);
                    rightel_back.setBackgroundResource(R.drawable.white_rounded_background);
                    operator = "1";

                    normalCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
                    normalCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));

                    initHamrahChargeList();


                } else if (isRightel(Numbers.ToEnglishNumbers(s.toString()))) {
                    rightel_back.setBackgroundResource(R.drawable.green_stroke_background);
                    hamrah_back.setBackgroundResource(R.drawable.white_rounded_background);
                    irancell_back.setBackgroundResource(R.drawable.white_rounded_background);
                    operator = "3";

                    initRightellChargeList();

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

//                normalCharge_back.setBackgroundResource(R.drawable.green_rounded_background);
//                normalCharge_text.setTextColor(getResources().getColor(R.color.colorWhite));
//                creditCharge.setBackgroundResource(R.drawable.green_rounded_background);
//                creditChargeText.setTextColor(getResources().getColor(R.color.colorWhite));
//
//                type = "0";
//                serviceType = 0;

                // TODO Auto-generated method stub
            }
        });

        preferredAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() != 0) {
                    String str = s.toString().replace(",", "");
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(Double.valueOf(Numbers.ToEnglishNumbers(str)));
                    preferredAmount.removeTextChangedListener(this);
                    preferredAmount.setText(Numbers.ToPersianNumbers(formattedNumber));
                    preferredAmount.setSelection(preferredAmount.length());
                    preferredAmount.addTextChangedListener(this);
                    //
                }
            }
        });

    }

    public void contactClicked(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }


    private void initIrancellChargeList() {
        ArrayList<ChargesList> chargesLists = new ArrayList<>();

        chargesLists.add(new ChargesList(R.drawable.irancell_logo_green, Numbers.ToPersianNumbers("10,000")));
        chargesLists.add(new ChargesList(R.drawable.irancell_logo_green, Numbers.ToPersianNumbers("20,000")));
        chargesLists.add(new ChargesList(R.drawable.irancell_logo_green, Numbers.ToPersianNumbers("50,000")));
        chargesLists.add(new ChargesList(R.drawable.irancell_logo_green, Numbers.ToPersianNumbers("100,000")));

        // set up the RecyclerView
        chargeList.setLayoutManager(new LinearLayoutManager(this));
        chargeAdapter = new ChargeAmountAdapter(this, chargesLists);
        chargeAdapter.setClickListener(this);
        chargeList.setAdapter(chargeAdapter);
    }

    private void initRightellChargeList() {
        ArrayList<ChargesList> chargesLists = new ArrayList<>();

        chargesLists.add(new ChargesList(R.drawable.rightel_logo, Numbers.ToPersianNumbers("10,000")));
        chargesLists.add(new ChargesList(R.drawable.rightel_logo, Numbers.ToPersianNumbers("20,000")));
        chargesLists.add(new ChargesList(R.drawable.rightel_logo, Numbers.ToPersianNumbers("50,000")));
        chargesLists.add(new ChargesList(R.drawable.rightel_logo, Numbers.ToPersianNumbers("100,000")));

        // set up the RecyclerView
        chargeList.setLayoutManager(new LinearLayoutManager(this));
        chargeAdapter = new ChargeAmountAdapter(this, chargesLists);
        chargeAdapter.setClickListener(this);
        chargeList.setAdapter(chargeAdapter);
    }

    private void initHamrahChargeList() {
        ArrayList<ChargesList> chargesLists = new ArrayList<>();

        chargesLists.add(new ChargesList(R.drawable.hamrah_aval_logo_green, Numbers.ToPersianNumbers("10,000")));
        chargesLists.add(new ChargesList(R.drawable.hamrah_aval_logo_green, Numbers.ToPersianNumbers("20,000")));
        chargesLists.add(new ChargesList(R.drawable.hamrah_aval_logo_green, Numbers.ToPersianNumbers("50,000")));
        chargesLists.add(new ChargesList(R.drawable.hamrah_aval_logo_green, Numbers.ToPersianNumbers("100,000")));

        // set up the RecyclerView
        chargeList.setLayoutManager(new LinearLayoutManager(this));
        chargeAdapter = new ChargeAmountAdapter(this, chargesLists);
        chargeAdapter.setClickListener(this);
        chargeList.setAdapter(chargeAdapter);
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

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class GetChargeType extends AsyncTask<Void, Void, String> {

        String results = null;

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
        protected String doInBackground(Void... params) {
            results = new Caller().getChargeType(userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(String chargeType) {
            super.onPostExecute(chargeType);
            //TODO we should add other items here too


            if (!chargeType.equals("anyType")) {


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
