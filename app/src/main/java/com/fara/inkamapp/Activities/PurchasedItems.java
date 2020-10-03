package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.ChargeAmountAdapter;
import com.fara.inkamapp.Adapters.NotificationAdapter;
import com.fara.inkamapp.Adapters.PurchasedAdapter;
import com.fara.inkamapp.Adapters.PurchasedTopicsAdapter;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ChargesList;
import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.Models.Report;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class PurchasedItems extends AppCompatActivity {

    private RecyclerView typeRecyclerView, purchasedRecyclerView;
    private PurchasedTopicsAdapter purchasedTopicsAdapter;
    private PurchasedAdapter purchasedAdapter;
    private int purchasedType, adapterType;
    private TextView toastText, empty;
    private SharedPreferences sharedPreferences;
    private String _userId, _token, _fromDate, _toDate;
    private ImageButton back;

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

        setContentView(R.layout.activity_purchased_items);

        initVariables();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            purchasedType = bundle.getInt("purchasedType");
        switch (purchasedType) {
            case 1:
                adapterType = 0;
                break;

            case 3:
                adapterType = 1;

                break;

            case 4:
                adapterType = 2;

                break;

            case 7:
                adapterType = 3;

                break;

            case 8:
                adapterType = 4;
                break;

            case 9:
                adapterType = 5;
                break;


            default:
                break;

        }
        initPurchasedType();

    }

    private void initVariables() {
        typeRecyclerView = findViewById(R.id.rv_purchased_topics);
        purchasedRecyclerView = findViewById(R.id.rv_all_purchased);
        back = findViewById(R.id.ib_back);
        empty = findViewById(R.id.tv_empty);

        back.setOnClickListener(view -> onBackPressed());

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            _userId = sharedPreferences.getString("UserID", null);
            _token = Base64.encode((RSA.encrypt(sharedPreferences.getString("Token", null), publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        _fromDate = "";
        _toDate = "";


    }

    private void initPurchasedType() {
        ArrayList<String> typeList = new ArrayList<>();

//        typeList.add("همه");
        typeList.add("بسته");
        typeList.add("شارژ");
        typeList.add("اتوبوس");
        typeList.add("خلافی خودرو");
        typeList.add("تلفن");
        typeList.add("قبوض خدماتی");

//        typeList.add("قطار");
//        typeList.add("هواپیما");


        // set up the RecyclerView
        typeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        purchasedTopicsAdapter = new PurchasedTopicsAdapter(this, typeList, adapterType);
        new GetAllReports().execute();

        purchasedTopicsAdapter.setClickListener((view, position) -> {
            switch (position) {
                case 0:

                    purchasedType = 1;
                    new GetAllReports().execute();

                    break;
                case 1:
                    purchasedType = 3;
                    new GetAllReports().execute();

                    break;
                case 2:
                    purchasedType = 4;
                    new GetAllReports().execute();

                    break;
                case 3:
                    purchasedType = 7;
                    new GetAllReports().execute();

                    break;
                case 4:
                    purchasedType = 8;
                    new GetAllReports().execute();

                    break;

                case 5:
                    purchasedType = 9;
                    new GetAllReports().execute();

                    break;

                case 6:

                    break;


            }

        });
        typeRecyclerView.setAdapter(purchasedTopicsAdapter);
        new GetAllReports().execute();

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class GetAllReports extends AsyncTask<Void, Void, ArrayList<Report>> {

        ArrayList<Report> results = null;

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
        protected ArrayList<Report> doInBackground(Void... params) {
            results = new Caller().getAllTransactionReport(_userId, _token, _fromDate, _toDate, purchasedType);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Report> reports) {
            super.onPostExecute(reports);
            //TODO we should add other items here too

            if (reports != null) {
                if (reports.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                    purchasedRecyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    purchasedRecyclerView.setVisibility(View.VISIBLE);
                    purchasedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    purchasedAdapter = new PurchasedAdapter(getApplicationContext(), reports);
                    purchasedRecyclerView.setAdapter(purchasedAdapter);
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

}
