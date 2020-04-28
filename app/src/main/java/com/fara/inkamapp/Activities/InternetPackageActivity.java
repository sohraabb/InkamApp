package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.NetPackages;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.PackageDataPlanListByPeriod;
import com.fara.inkamapp.Models.PackagesDataPlan;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class InternetPackageActivity extends AppCompatActivity implements NetPackages.ItemClickListener {

    private NetPackages netPackagesAdapter;
    private RelativeLayout packageInfo;
    private TextView toastText;
    private JSONObject postData;
    private String phoneNumber;
    private int operatorType;

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

        setContentView(R.layout.activity_internet_package);
        packageInfo = findViewById(R.id.rl_package_info);

        Bundle intent = getIntent().getExtras();
        operatorType = intent.getInt("Operator");
        phoneNumber = intent.getString("CellNumbers");


        packageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                postData = new JSONObject();
//                try {
//                    postData.put("CellNumbers", phoneNumber);
//                    postData.put("ChargeType", "0");
//                    postData.put("BankId", "08");
//                    postData.put("DeviceType", "59");
//                    postData.put("Operator", operatorType);
//                    postData.put("Merchant", "irancell");
//                    postData.put("packageDescription", "0");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                new GetDataPlanListByPeriod().execute();


            }
        });

        // data to populate the RecyclerView with

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("ساعتی");
        animalNames.add("روزانه");
        animalNames.add("هفتگی");
        animalNames.add("ماهانه");
        animalNames.add("سالانه");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_packages);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        netPackagesAdapter = new NetPackages(getApplicationContext(), animalNames);
        netPackagesAdapter.setClickListener(this);
        recyclerView.setAdapter(netPackagesAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + netPackagesAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class GetDataPlanListByPeriod extends AsyncTask<Void, Void, ArrayList<PackagesDataPlan>> {

        ArrayList<PackagesDataPlan> results = null;

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
        protected ArrayList<PackagesDataPlan> doInBackground(Void... params) {
            results = new Caller().getDataPlanListByPeriod(operatorType, 0, 0,"2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<PackagesDataPlan> packagesDataPlans) {
            super.onPostExecute(packagesDataPlans);
            //TODO we should add other items here too

            if (packagesDataPlans != null) {

                Intent intent = new Intent(getApplicationContext(), BuyPackages.class);
//                intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
//                intent.putExtra("data", "");
                startActivity(intent);

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
