package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.NetPackages;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.OperatorType;
import com.fara.inkamapp.Models.PackageDataPlanListByPeriod;
import com.fara.inkamapp.Models.PackagesDataPlan;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
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

public class InternetPackageActivity extends AppCompatActivity implements NetPackages.ItemClickListener, View.OnClickListener {

    private NetPackages netPackagesAdapter;
    private RelativeLayout packageInfo, rv_tdl, rv_fix, rv_credit;
    private TextView toastText, tvTdl, tvFix, tvCredit, purchased;
    private JSONObject postData;
    private String phoneNumber, userID, encryptedToken, AesKey, token;
    private LinearLayout llDataplan;
    private SharedPreferences sharedpreferences;
    private Map internetPeriod;
    private Integer period;
    private int operatorType, planType, planID;
    private ImageButton back;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

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
        initVariables();

        new GetInternetPeriod().execute();
    }

    private void initVariables() {
        llDataplan = findViewById(R.id.ll_dataplan);
        rv_credit = findViewById(R.id.rv_credit);
        rv_fix = findViewById(R.id.rv_fix);
        rv_tdl = findViewById(R.id.rl_tdl);
        tvCredit = findViewById(R.id.tv_credit);
        tvFix = findViewById(R.id.tv_fix_sim);
        tvTdl = findViewById(R.id.tv_tdl);
        purchased = findViewById(R.id.tv_purchased);
        back = findViewById(R.id.ib_back);

        Bundle intent = getIntent().getExtras();
        operatorType = intent.getInt("Operator");
        phoneNumber = intent.getString("CellNumbers");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rv_credit.setOnClickListener(this);
        rv_fix.setOnClickListener(this);
        rv_tdl.setOnClickListener(this);
        purchased.setOnClickListener(this);

        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException |
                InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        period = Integer.valueOf((String) internetPeriod.keySet().toArray()[position]);
        new GetDataPlanListByPeriod().execute();
    }

    @Override
    public void onClick(View v) {
        rv_credit.setBackgroundResource(R.drawable.gray_rounded_background);
        tvCredit.setTextColor(getResources().getColor(R.color.colorBrown));
        rv_tdl.setBackgroundResource(R.drawable.gray_rounded_background);
        tvTdl.setTextColor(getResources().getColor(R.color.colorBrown));
        rv_fix.setBackgroundResource(R.drawable.gray_rounded_background);
        tvFix.setTextColor(getResources().getColor(R.color.colorBrown));
        switch (v.getId()) {
            case R.id.rv_credit:
                rv_credit.setBackgroundResource(R.drawable.green_rounded_background);
                tvCredit.setTextColor(getResources().getColor(R.color.colorWhite));

                planType = 0;

                break;
            case R.id.rv_fix:
                rv_fix.setBackgroundResource(R.drawable.green_rounded_background);
                tvFix.setTextColor(getResources().getColor(R.color.colorWhite));

                planType = 1;

                break;
            case R.id.rl_tdl:
                rv_tdl.setBackgroundResource(R.drawable.green_rounded_background);
                tvTdl.setTextColor(getResources().getColor(R.color.colorWhite));
                planType = 3;

                break;

            case R.id.tv_purchased:
                Intent intent = new Intent(getApplicationContext(), PurchasedItems.class);
                intent.putExtra("purchasedType", 1);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    private boolean isTDLTE(String input) {
        Pattern p = Pattern.compile("^09[4][1][0-9]{7}$");
        Matcher m = p.matcher(input);
        return m.matches();
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
            results = new Caller().getDataPlanListByPeriod(operatorType, period, planType, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<PackagesDataPlan> packagesDataPlans) {
            super.onPostExecute(packagesDataPlans);
            //TODO we should add other items here too

            if (packagesDataPlans != null) {
                llDataplan.removeAllViews();
                for (int i = 0; i < packagesDataPlans.size(); i++) {
                    addView(packagesDataPlans.get(i));
                }
                //  Intent intent = new Intent(getApplicationContext(), BuyPackages.class);
//                intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
//                intent.putExtra("data", "");
                // startActivity(intent);

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

    private class GetInternetPeriod extends AsyncTask<Void, Void, Map> {

        Map results = new HashMap();

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
        protected Map doInBackground(Void... params) {
            results = new Caller().GetInternetPeriod(userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(Map packagesDataPlans) {
            super.onPostExecute(packagesDataPlans);
            //TODO we should add other items here too

            if (packagesDataPlans != null) {
                internetPeriod = packagesDataPlans;

                // set up the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rv_packages);
                LinearLayoutManager horizontalLayoutManager
                        = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
                recyclerView.setLayoutManager(horizontalLayoutManager);
                netPackagesAdapter = new NetPackages(getApplicationContext(), packagesDataPlans);
                netPackagesAdapter.setClickListener(InternetPackageActivity.this);
                recyclerView.setAdapter(netPackagesAdapter);

                if (isTDLTE(phoneNumber)) {
                    period = Integer.valueOf((String) internetPeriod.keySet().toArray()[4]);
                    new GetDataPlanListByPeriod().execute();
                    netPackagesAdapter.notifyDataSetChanged();
                    rv_tdl.setBackgroundResource(R.drawable.green_rounded_background);
                    tvTdl.setTextColor(getResources().getColor(R.color.colorWhite));
                    planType = 3;
                } else {

                    period = Integer.valueOf((String) internetPeriod.keySet().toArray()[4]);
                    new GetDataPlanListByPeriod().execute();
                    netPackagesAdapter.notifyDataSetChanged();
                    netPackagesAdapter.setClickListener(InternetPackageActivity.this);
                    rv_credit.setBackgroundResource(R.drawable.green_rounded_background);
                    tvCredit.setTextColor(getResources().getColor(R.color.colorWhite));

                    planType = 0;
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

    private void addView(final PackagesDataPlan plan) {
        View view = LayoutInflater.from(this).inflate(R.layout.data_plan_items, null);

        TextView tvPrice = view.findViewById(R.id.tv_price);
        ImageView ivLogo = view.findViewById(R.id.iv_logo);
        TextView tvDescription = view.findViewById(R.id.tv_package_content);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(plan.get_priceWithTax());
        tvPrice.setText(Numbers.ToPersianNumbers(formattedNumber));
        tvDescription.setText(Numbers.ToPersianNumbers(plan.get_title()));
        if (operatorType == 0) {
            ivLogo.setImageResource(R.drawable.irancell_logo_green);
        } else if (operatorType == 1) {
            ivLogo.setImageResource(R.drawable.hamrah_aval_logo_green);
        } else if (operatorType == 2) {
            ivLogo.setImageResource(R.drawable.rightel_logo);
        }
        llDataplan.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planID = plan.get_id();

                Bundle bundle = new Bundle();
                bundle.putString("amountWithTax", String.valueOf(plan.get_priceWithTax()));
                bundle.putString("amountWithoutTax", String.valueOf(plan.get_priceWithoutTax()));
                bundle.putString("operator", String.valueOf(operatorType));
                bundle.putString("type", String.valueOf(planID));
                bundle.putString("phone", phoneNumber);
                bundle.putString("describe", plan.get_title());
                bundle.putString("dataPlanType", String.valueOf(plan.get_dataPlanType()));
                bundle.putInt("serviceType", 4);


                bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());


            }
        });
    }
}
