package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.Bus;
import com.fara.inkamapp.Models.BusSummary;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class BusTicketInfo extends AppCompatActivity {
    private TextView toastText;
    private String busID;
    private String from;
    private String to;
    private TextView tvName;
    private TextView busType;
    private TextView busEndOrigin;
    private TextView busEndDestination;
    private TextView tvAdultPrice;
    private TextView tvCurrentLocation;
    private TextView tvDestination;
    private TextView tvDay;
    private TextView tvDate;
    private TextView tvCurrentLocTime, tvDiscount;
    private TextView tvDestinationTime;
    private TextView tvNumberPassengers;
    private RelativeLayout rvDiscount;
    private String destName, destinationID, sourceID;
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

        setContentView(R.layout.activity_bus_ticket_info);
        initVariables();

        new GetSpecificService().execute();
    }

    private void initVariables() {
        tvName = findViewById(R.id.tv_name);
        busType = findViewById(R.id.bus_type);
        busEndOrigin = findViewById(R.id.bus_end_origin);
        busEndDestination = findViewById(R.id.bus_end_destination);
        tvAdultPrice = findViewById(R.id.tv_adult_price);
        tvCurrentLocation = findViewById(R.id.tv_current_location);
        tvDestination = findViewById(R.id.tv_destination);
        tvDay = findViewById(R.id.tv_day);
        tvDate = findViewById(R.id.tv_date);
        tvCurrentLocTime = findViewById(R.id.tv_current_loc_time);
        tvDestinationTime = findViewById(R.id.tv_destination_time);
        tvNumberPassengers = findViewById(R.id.tv_number_passengers);
        tvDiscount = findViewById(R.id.tv_price_discount);
        rvDiscount = findViewById(R.id.rl_discount);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        busID = getIntent().getStringExtra("busID");
        String fromDate = getIntent().getStringExtra("fromDate");
        destName = getIntent().getStringExtra("toName");
        String sourceName = getIntent().getStringExtra("fromName");
        String time = getIntent().getStringExtra("time");
        sourceID = getIntent().getStringExtra("sourceID");
        destinationID = getIntent().getStringExtra("destinationID");
        tvCurrentLocation.setText(sourceName);
        tvDestination.setText(destName);

        ///
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        tvCurrentLocTime.setText(Numbers.ToPersianNumbers(time.split("T")[1].substring(0, 5)));

        ///

        String[] separateDate = fromDate.replace("T", " ").split(" ")[0].split("-");

        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tvDate.setText(Numbers.ToPersianNumbers(currentPersianDate));
        tvDay.setText(j.getPersianLongDate().split(" ")[0]);
        ///


        Button btnSelect = findViewById(R.id.btn_select_ticket);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusTicketSelectionProcess.class);
                intent.putExtra("busID", busID);
                intent.putExtra("toCity", destName);
                intent.putExtra("sourceID", sourceID);
                intent.putExtra("destinationID", destinationID);
                startActivity(intent);

            }
        });
    }

    private class GetSpecificService extends AsyncTask<Void, Void, Bus> {

        Bus results = null;

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
        protected Bus doInBackground(Void... params) {


            results = new Caller().getSpecificService(userID, encryptedToken, busID);

            return results;
        }

        @Override
        protected void onPostExecute(final Bus bus) {
            super.onPostExecute(bus);
            //TODO we should add other items here too
            if (bus == null) {

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
                    tvName.setText(bus.getCompany());
                    busType.setText(bus.getType());
                    busEndOrigin.setText(bus.getBoardingPoint().getTerminal());
                    busEndDestination.setText(bus.getDroppingPoints().get(bus.getDroppingPoints().size() - 1).getTerminal());
                    ///
                    float discount = bus.getFinancial().getMaxApplicableDiscountPercentage();
                    if (discount > 0) {
                        rvDiscount.setVisibility(View.VISIBLE);
                    }
                    int price = bus.getFinancial().getPrice();

                    DecimalFormat sdd = new DecimalFormat("#,###");
                    Double doubleNumber = Double.parseDouble(String.valueOf(bus.getFinancial().getPrice()));
                    Double doubleNumber2 = Double.parseDouble(String.valueOf(price - (price * discount / 100)));

                    String format = sdd.format(doubleNumber);
                    ///
                    tvDiscount.setText(Numbers.ToPersianNumbers(format));
                    tvAdultPrice.setText(Numbers.ToPersianNumbers(sdd.format(doubleNumber2)));
                    tvCurrentLocation.setText(bus.getBoardingPoint().getCity());
                    tvDestination.setText(bus.getDroppingPoints().get(0).getCity());


                } catch (Exception ex) {
                    Log.e("errorrrrrrrrr", ex.toString());
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    public void backClicked(View v) {
        finish();
    }
}
