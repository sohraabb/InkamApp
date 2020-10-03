package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.AirportCityAdapter;
import com.fara.inkamapp.Adapters.CityAdapter;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.City;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class AirplaneTickets extends AppCompatActivity implements
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener,
        AirportCityAdapter.CityCustomListener {

    private Button search;
    private TextView toastText;
    private JSONObject postData;
    private AutoCompleteTextView sourceLocation;
    private AutoCompleteTextView destination;
    private String destinationID;
    private String sourceID, fightType;
    private CityAdapter sourceAdapter;
    private CityAdapter destinationAdapter;
    private EditText et_date_leave;
    private EditText et_date_return;
    private DatePickerDialog picker;
    private EditText selected;
    private ImageView ivBackForth;
    private String token, userID, encryptedToken, AesKey, dataToConfirm;
    private SharedPreferences sharedpreferences;


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

        setContentView(R.layout.activity_air_plane_tickets);
        initVariables();

        new GetCities().execute();

    }

    private void initVariables() {
        sourceLocation = findViewById(R.id.et_your_location);
        destination = findViewById(R.id.et_your_destination);
        et_date_leave = findViewById(R.id.et_date_leave);
        et_date_return = findViewById(R.id.et_date_return);
        ivBackForth = findViewById(R.id.iv_back_forth);
        search = findViewById(R.id.btn_search);
        RelativeLayout rlChange = findViewById(R.id.rl_change);
        final TextView tvLeaveReturn = findViewById(R.id.tv_leave_return);
        final TextView tvLeave = findViewById(R.id.tv_leave);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        fightType = "1";
        et_date_leave.setInputType(InputType.TYPE_NULL);

        tvLeaveReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLeaveReturn.setBackgroundResource(R.drawable.button_background_green);
                tvLeave.setTextColor(getResources().getColor(R.color.colorBrown));
                tvLeave.setBackgroundResource(0);
                tvLeaveReturn.setTextColor(getResources().getColor(R.color.colorWhite));
                et_date_return.setVisibility(View.VISIBLE);
                fightType = "2";
            }
        });
        et_date_return.setVisibility(View.INVISIBLE);
        tvLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLeave.setBackgroundResource(R.drawable.button_background_green);
                tvLeave.setTextColor(getResources().getColor(R.color.colorWhite));
                tvLeaveReturn.setTextColor(getResources().getColor(R.color.colorBrown));
                tvLeaveReturn.setBackgroundResource(0);
                fightType = "1";
                et_date_return.setVisibility(View.INVISIBLE);
            }
        });
        rlChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = "";
                String tmp = "";
                tmp = sourceID;
                temp = sourceLocation.getText().toString();
                sourceLocation.setText(destination.getText().toString());
                destination.setText(temp);

                sourceID = destinationID;
                destinationID = tmp;
            }
        });

        et_date_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected = (EditText) v;
                showCalendar();

            }
        });
        et_date_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected = (EditText) v;
                showCalendar();

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  String toDate = DateConverter.Convert_Shamsi_To_Miladi_Date(et_date_return.getText().toString(), 0, 0);
                if (sourceID == null) {
                    setToast(getString(R.string.select_resource_city));
                    return;
                }
                if (destinationID == null) {
                    setToast(getString(R.string.select_destinationCity));
                    return;
                }
                if (et_date_leave.getText().toString().length() == 0) {
                    setToast(getString(R.string.choose_date));
                    return;
                }
                String fromDate = DateConverter.Convert_Shamsi_To_Miladi_Date(et_date_leave.getText().toString(), 0, 0);

                Intent intent = new Intent(getApplicationContext(), SearchTickets.class);
                intent.putExtra("from", sourceID);
                intent.putExtra("to", destinationID);
                intent.putExtra("fromName", sourceLocation.getText().toString());
                intent.putExtra("toName", destination.getText().toString());
                intent.putExtra("date", fromDate.split(" ")[0]);
                if (fightType.equals("2")) {
                    String returnDate = DateConverter.Convert_Shamsi_To_Miladi_Date(et_date_return.getText().toString(), 0, 0);
                    intent.putExtra("returnDate", returnDate.split(" ")[0]);
                }

                intent.putExtra("flightType", fightType);

                startActivity(intent);
//                new BookBusTicket().execute();

            }
        });

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    public void showCalendar() {
        //  Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANYekanRegular.ttf");

        PersianCalendar persianCalendar = new PersianCalendar();


        //  persianCalendar.setPersianDate(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));


        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog datePickerDialog = com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

        selected.setText(date);
    }

    @Override
    public void customOnItemClick(String key) {
        sourceID = key;
    }

    private class GetCities extends AsyncTask<Void, Void, Map<String, String>> {

        Map<String, String> results = null;

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
        protected Map<String, String> doInBackground(Void... params) {
            results = new Caller().getAirportCities(userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(Map<String, String> cityList) {
            super.onPostExecute(cityList);
            //TODO we should add other items here too
            if (cityList != null && cityList.isEmpty()) {

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
            } else {
                try {
                    List<City> list = new ArrayList<>();

                    for (String items : cityList.keySet()) {
                        City city = new City();
                        city.set_id(items);
                        city.set_name(cityList.get(items));
                        list.add(city);
                    }

                    sourceAdapter = new CityAdapter(getApplicationContext(), list);
                    sourceLocation.setAdapter(sourceAdapter);
                    sourceLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            City city = sourceAdapter.getItem(i);
                            if (city != null)
                                sourceID = city.get_id();

                        }
                    });
                    destinationAdapter = new CityAdapter(getApplicationContext(), list);
                    destination.setAdapter(destinationAdapter);
                    destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            City city = sourceAdapter.getItem(i);
                            if (city != null)
                                destinationID = city.get_id();
                        }
                    });
                } catch (Exception e) {
                    String g = e.toString();
                }
            }
        }
    }

    public void setToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
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
