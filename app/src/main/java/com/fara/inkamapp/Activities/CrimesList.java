package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.CrimeAdapter;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.CarFinesParameters;
import com.fara.inkamapp.Models.PayInfo;
import com.fara.inkamapp.Models.PaymentResult;
import com.fara.inkamapp.Models.TrafficFines;
import com.fara.inkamapp.Models.TrafficFinesDetails;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class CrimesList extends AppCompatActivity  {
    private String token, userID, encryptedToken, AesKey, dataToConfirm, fineToken;
    private TextView scanBarcode, toastText;
    private SharedPreferences sharedpreferences;
    private RecyclerView rv_crimes;
    private List<TrafficFinesDetails> selectedFine;
    private TrafficFines fines;
    private CrimeAdapter adapter;
    private ImageButton back;
    private Boolean allSelected;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    public CrimesList() {
    }

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

        setContentView(R.layout.activity_crimes_list);
        allSelected = false;
        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        selectedFine = new ArrayList<>();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        fineToken = getIntent().getStringExtra("fineToken");
        rv_crimes = findViewById(R.id.rv_crimes);
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
        new trafficFinesInfo().execute();
    }

    private class trafficFinesInfo extends AsyncTask<Void, Void, TrafficFines> {

        TrafficFines results = null;

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
        protected TrafficFines doInBackground(Void... params) {

            results = new Caller().getTrafficFinesInfo(fineToken, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(TrafficFines trafficFines) {
            super.onPostExecute(trafficFines);
            //TODO we should add other items here too

            try {
                if (trafficFines != null) {
                    fines = trafficFines;
                    if (trafficFines.get_status().get_code().matches("G00000")) {
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rv_crimes.setLayoutManager(layoutManager);
                        adapter = new CrimeAdapter(getApplicationContext(), trafficFines.get_carFinesParameters());
                        rv_crimes.setAdapter(adapter);
                        adapter.setClickListener(new CrimeAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TrafficFinesDetails fine = fines.get_carFinesParameters().get_trafficFinesDetails().get(position);
                                if (selectedFine.contains(fine)) {
                                    selectedFine.remove(fine);
                                } else {
                                    selectedFine.add(fine);
                                }
                            }
                        });


                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), trafficFines.get_status().get_description(), Toast.LENGTH_SHORT);
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

            } catch (Exception e) {
                e.toString();
            }
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }
    public void onBackClicked(View v){
        finish();
    }


    public void onAllSelect(View v) {
        try {


            selectedFine.clear();

            if (!allSelected) {
                //selectedFine = fines.get_carFinesParameters().get_trafficFinesDetails();
                for (TrafficFinesDetails det :
                        fines.get_carFinesParameters().get_trafficFinesDetails()) {
                    selectedFine.add(det);
                }
            }

            adapter.selectAll(allSelected);
            allSelected = !allSelected;
        } catch (Exception e) {
            e.toString();
        }

    }

    public  void payFine(View v){

        try {

            Bundle bundle = new Bundle();
            bundle.putString("title", getResources().getString(R.string.car_fines));
            bundle.putString("plateNumber", fines.get_carFinesParameters().get_plateNumber());
            bundle.putString("amount",String.valueOf( selectedFine.get(0).get_amount()));
            bundle.putString("paymentID", selectedFine.get(0).get_paymentId());
            bundle.putString("billID", selectedFine.get(0).get_billId());
            bundle.putString("city", selectedFine.get(0).get_city());
            bundle.putString("location", selectedFine.get(0).get_location());
            bundle.putString("datetime", selectedFine.get(0).get_dateTime());
            bundle.putString("type", selectedFine.get(0).get_type());
            bundle.putInt("serviceType", 2);


            bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
            bottomSheetDialogFragment.setArguments(bundle);
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

        } catch (Exception e) {
            e.toString();
        }

    }

}
