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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.AutoProvinceAdapter;
import com.fara.inkamapp.Adapters.CityAdapter;
import com.fara.inkamapp.Adapters.DashboardServiceAdapter;
import com.fara.inkamapp.Adapters.ProvinceAdapter;
import com.fara.inkamapp.Adapters.ProvinceByCityAdapter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Models.City;
import com.fara.inkamapp.Models.Province;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SearchProvince extends HideKeyboard {
    private TextView toastText;
    private ProvinceAdapter pAdapter;
    private ProvinceByCityAdapter cAdapter;
    private RecyclerView rvProvince;
    private String _cityID, _cityName, _provinceID;
    private ProgressBar loadingProgress;
    private AutoCompleteTextView search;
    private String sourceID;
    private AutoProvinceAdapter autoProvinceAdapter;
    private CityAdapter cityAdapter;

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

        setContentView(R.layout.activity_search_province);
        initVariables();

        new GetProvince().execute();

    }

    private void initVariables() {
        rvProvince = findViewById(R.id.rv_province);
        loadingProgress = findViewById(R.id.progress_loader);
        search = findViewById(R.id.et_search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class GetProvince extends AsyncTask<Void, Void, ArrayList<Province>> {

        ArrayList<Province> results = null;

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
        protected ArrayList<Province> doInBackground(Void... params) {
            results = new Caller().getProvince();

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Province> provinces) {
            super.onPostExecute(provinces);
            //TODO we should add other items here too

            if (provinces != null) {

                List<String> list = new ArrayList<>();
                for (Province obj : provinces) {
                    list.add(obj.get_name());
                }
                autoProvinceAdapter = new AutoProvinceAdapter(getApplicationContext(), provinces);
                search.setAdapter(autoProvinceAdapter);
                search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Province city = autoProvinceAdapter.getItem(i);
                        _provinceID = city.get_id();
                        search.setText("");
                        search.setHint(R.string.your_city);
                        new GetCityByProvinceId().execute();
                    }
                });

                pAdapter = new ProvinceAdapter(getApplicationContext(), provinces);
                rvProvince.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvProvince.setAdapter(pAdapter);
                pAdapter.setClickListener(new ProvinceAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        _provinceID = pAdapter.getItem(position).get_id();
                        new GetCityByProvinceId().execute();
                        search.setHint(R.string.your_city);
                    }
                });
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

    private class GetCityByProvinceId extends AsyncTask<Void, Void, ArrayList<City>> {

        ArrayList<City> results = null;

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
            } else {
                loadingProgress.setVisibility(View.VISIBLE);
                loadingProgress.setActivated(true);
            }

        }

        @Override
        protected ArrayList<City> doInBackground(Void... params) {
            results = new Caller().getCityByProvince(_provinceID);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);
            //TODO we should add other items here too

            if (cities != null) {


                List<String> list = new ArrayList<>();
                for (City obj : cities) {
                    list.add(obj.get_name());
                }
                cityAdapter = new CityAdapter(getApplicationContext(), cities);
                search.setAdapter(cityAdapter);
                search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        City city = (City) cityAdapter.getItem(i);
                        if (city != null) {
                            _cityID = city.get_id();
                            _cityName = city.get_name();

                            Intent intent = new Intent();
                            intent.putExtra("CityID", _cityID);
                            intent.putExtra("CityName", _cityName);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });


                cAdapter = new ProvinceByCityAdapter(getApplicationContext(), cities);
                rvProvince.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvProvince.setAdapter(cAdapter);
                cAdapter.setClickListener(new ProvinceByCityAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        _cityID = cAdapter.getItem(position).get_id();
                        _cityName = cAdapter.getItem(position).get_name();

                        Intent intent = new Intent();
                        intent.putExtra("CityID", _cityID);
                        intent.putExtra("CityName", _cityName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
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

            loadingProgress.setActivated(false);
            loadingProgress.setVisibility(View.GONE);
        }
    }
}