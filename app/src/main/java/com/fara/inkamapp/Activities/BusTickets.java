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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.CityAdapter;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.BookTicket;
import com.fara.inkamapp.Models.City;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONObject;

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

public class BusTickets extends HideKeyboard implements
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private Button search;
    private TextView toastText, tvLeave, tvLeaveReturn, purchased;
    private JSONObject postData;
    private AutoCompleteTextView sourceLocation;
    private AutoCompleteTextView destination;
    private String destinationID;
    private String sourceID;
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

        setContentView(R.layout.activity_bus_tickets);

        tvLeave = findViewById(R.id.tv_leave);
        tvLeaveReturn = findViewById(R.id.tv_leave_return);
        sourceLocation = findViewById(R.id.et_your_location);
        destination = findViewById(R.id.et_your_destination);
        et_date_leave = findViewById(R.id.et_date_leave);
        et_date_return = findViewById(R.id.et_date_return);
        ivBackForth = findViewById(R.id.iv_back_forth);
        purchased = findViewById(R.id.tv_purchased);

        purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PurchasedItems.class);
                intent.putExtra("purchasedType", 4);
                startActivity(intent);
            }
        });

        ivBackForth.setOnClickListener(new View.OnClickListener() {
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

        sourceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sourceLocation.setText("");
            }
        });

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination.setText("");
            }
        });

     /*   tvLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLeave.setBackgroundResource(R.drawable.button_background_green);
                tvLeave.setTextColor(getResources().getColor(R.color.colorWhite));
                tvLeaveReturn.setTextColor(getResources().getColor(R.color.colorBrown));
                tvLeaveReturn.setBackgroundResource(0);
                et_date_return.setVisibility(View.INVISIBLE);
            }
        });
        tvLeaveReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLeaveReturn.setBackgroundResource(R.drawable.button_background_green);
                tvLeave.setTextColor(getResources().getColor(R.color.colorBrown));
                tvLeave.setBackgroundResource(0);
                tvLeaveReturn.setTextColor(getResources().getColor(R.color.colorWhite));
                et_date_return.setVisibility(View.VISIBLE);
            }
        });*/
        et_date_leave.setInputType(InputType.TYPE_NULL);


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
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        search = findViewById(R.id.btn_search);
        new GetCities().execute();
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
                try {


                    Intent intent = new Intent(getApplicationContext(), BusServices.class);
                    intent.putExtra("from", sourceID);
                    intent.putExtra("to", destinationID);
                    intent.putExtra("fromName", sourceLocation.getText().toString());
                    intent.putExtra("toName", destination.getText().toString());
                    intent.putExtra("date", fromDate);

                    startActivity(intent);
                } catch (Exception e) {
                    Log.i("BusTicketIncome", e.toString());
                }
//                new BookBusTicket().execute();

            }
        });
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    public void showCalendar() {
        //  Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf");

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

    private class BookBusTicket extends AsyncTask<Void, Void, BookTicket> {

        BookTicket results = null;

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
        protected BookTicket doInBackground(Void... params) {
            String res = new Caller().bookBusTicket(userID, encryptedToken, postData.toString());
            try {
                String dec = AESEncyption.decryptMsg(res, AesKey);
                Gson g = new Gson();
                // g.fromJson(dec,)

            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(BookTicket bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket.getToken() != null) {
                Intent intent = new Intent(getApplicationContext(), BusTicketInfo.class);
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

    private class GetCities extends AsyncTask<Void, Void, List<City>> {

        List<City> results = null;

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
        protected List<City> doInBackground(Void... params) {
            results = new Caller().GetCities(userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(List<City> cityList) {
            super.onPostExecute(cityList);
            //TODO we should add other items here too
            if (cityList.isEmpty()) {

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
                List<String> list = new ArrayList<>();
                for (City obj : cityList) {
                    list.add(obj.get_name());
                }
                sourceAdapter = new CityAdapter(getApplicationContext(), cityList);
                sourceLocation.setAdapter(sourceAdapter);
                sourceLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        City city = (City) sourceAdapter.getItem(i);
                        sourceID = city.get_id();
                    }
                });
                destinationAdapter = new CityAdapter(getApplicationContext(), cityList);
                destination.setAdapter(destinationAdapter);
                destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        City city = (City) destinationAdapter.getItem(i);
                        destinationID = city.get_id();
                    }
                });
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

    public void backClicked(View v) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }
}
