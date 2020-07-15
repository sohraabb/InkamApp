package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private RelativeLayout rightel_back, irancell_back, hamrah_back, normalCharge_back, amazingCharge_back;
    private TextView toastText, amazingCharge_text, normalCharge_text;
    private JSONObject postData;
    private EditText phoneNumber;
    private RecyclerView chargeList;
    private ChargeAmountAdapter chargeAdapter;
    private SharedPreferences sharedpreferences;
    private String token, userID, encryptedToken;
    private String operator, type;
    private ImageButton back;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
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

        setContentView(R.layout.activity_buy_charge);


        phoneNumber = findViewById(R.id.et_enter_phone_number);
        rightel_back = findViewById(R.id.rl_rightel);
        hamrah_back = findViewById(R.id.rl_hamrah_aval);
        irancell_back = findViewById(R.id.rl_irancell);
        chargeList = findViewById(R.id.rv_charges);
        normalCharge_back = findViewById(R.id.rl_normal_charge);
        amazingCharge_back = findViewById(R.id.rl_amazing_charge);
        normalCharge_text = findViewById(R.id.tv_normal_charge);
        amazingCharge_text = findViewById(R.id.tv_amazing_charge);

        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
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


        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isIrancell(s.toString())) {
                    irancell_back.setBackgroundResource(R.drawable.green_stroke_background);
                    hamrah_back.setBackgroundResource(R.drawable.white_rounded_background);
                    rightel_back.setBackgroundResource(R.drawable.white_rounded_background);
                    operator = "0";
                    initIrancellChargeList();

                } else if (isHamrahAval(s.toString())) {
                    hamrah_back.setBackgroundResource(R.drawable.green_stroke_background);
                    irancell_back.setBackgroundResource(R.drawable.white_rounded_background);
                    rightel_back.setBackgroundResource(R.drawable.white_rounded_background);
                    operator = "1";
                    initHamrahChargeList();


                } else if (isRightel(s.toString())) {
                    rightel_back.setBackgroundResource(R.drawable.green_stroke_background);
                    hamrah_back.setBackgroundResource(R.drawable.white_rounded_background);
                    irancell_back.setBackgroundResource(R.drawable.white_rounded_background);
                    operator = "3";
                    initRightellChargeList();

                }

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

//        new GetChargeType().execute();

//        charge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                postData = new JSONObject();
//                try {
//                    postData.put("Amount", "10000");
//                    postData.put("CellNumbers", "09374227117");
//                    postData.put("CellNumber", "9374227117");
//                    postData.put("ChargeType", "0");
//                    postData.put("BankId", "08");
//                    postData.put("DeviceType", "59");
//                    postData.put("Operator", "0");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//

//                new TopUpRequest().execute();

//            }
//        });
    }

    public void contactClicked(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }else {
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

    @Override
    public void onItemClick(View view, int position) {

        try {

            Bundle bundle = new Bundle();
            bundle.putString("amount", chargeAdapter.getItem(position).get_amount());
            bundle.putString("operator", operator);
            bundle.putString("type", type);
            bundle.putString("phone", phoneNumber.getText().toString());
            bundle.putInt("serviceType",0);


            bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
            bottomSheetDialogFragment.setArguments(bundle);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        } catch (Exception e) {
            e.toString();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

            default:
                break;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }
}
