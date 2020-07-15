package com.fara.inkamapp.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.BusSearchIinfoAdapter;
import com.fara.inkamapp.Adapters.CarouselPagerAdapter;
import com.fara.inkamapp.Fragments.ItemFragment;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.BusSummary;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class BusServices extends HideKeyboard implements ItemFragment.SliderItemClickListener {
    private TextView toastText, tv_current_location, tv_destination;
    private String destinationID;
    private String sourceID;
    private String fromDate, fromName, toName;
    private RecyclerView listView;
    public TextView selectedDay, selectedDate, tvDate, tvDay;

    public final static int LOOPS = 1000;
    public CarouselPagerAdapter adapter;
    public ViewPager pager;
    private String  token, userID, encryptedToken, AesKey,dataToConfirm;
    private SharedPreferences sharedpreferences;
    public static int count = 60; //ViewPager items size
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 60;

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

        setContentView(R.layout.activity_bus_services);
        listView = (RecyclerView) findViewById(R.id.rv_services);
        sourceID = getIntent().getStringExtra("from");
        destinationID = getIntent().getStringExtra("to");
        fromDate = getIntent().getStringExtra("date");
        toName = getIntent().getStringExtra("toName");
        fromName = getIntent().getStringExtra("fromName");

        tv_current_location = (TextView) findViewById(R.id.tv_current_location);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tvDate = findViewById(R.id.tv_date);
        tvDay = findViewById(R.id.tv_day);
        tv_current_location.setText(fromName);
        tv_destination.setText(toName);

        //
        String[] separateDate = fromDate.split(" ")[0].split("-");

        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tvDate.setText(Numbers.ToPersianNumbers( currentPersianDate));
        tvDay.setText(j.getPersianLongDate().split(" ")[0]);
        /////////


        pager = (ViewPager) findViewById(R.id.vp_date);

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 3) * 2) + 60;
        pager.setPageMargin(-pageMargin);

        adapter = new CarouselPagerAdapter(this, getSupportFragmentManager(), fromDate);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(5);


        //////
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
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
        new AvailableServices().execute();

    }

    @Override
    public void onItemClicked(String date, TextView dayOfWeek, TextView date1) {
        if (selectedDay != null) {
            selectedDate.setTextColor(Color.BLACK);
            selectedDay.setTextColor(Color.BLACK);
        }
        selectedDate = date1;
        selectedDay = dayOfWeek;
        fromDate = date;
        String[] separateDate = fromDate.replace("T", " ").split(" ")[0].split("-");

        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tvDate.setText(Numbers.ToPersianNumbers( currentPersianDate));
        tvDay.setText(j.getPersianLongDate().split(" ")[0]);

        new AvailableServices().execute();
    }

    private class AvailableServices extends AsyncTask<Void, Void, List<BusSummary>> {

        List<BusSummary> results = null;

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
        protected List<BusSummary> doInBackground(Void... params) {
            try {
                fromDate = fromDate.replace(' ', 'T');
            }catch(Exception e){
                e.toString();
            }


            results = new Caller().getAvailableServices(userID, encryptedToken, sourceID, destinationID, fromDate);

            return results;
        }

        @Override
        protected void onPostExecute(final List<BusSummary> busList) {
            super.onPostExecute(busList);
            //TODO we should add other items here too
            if (busList.isEmpty()) {

                Toast toast = Toast.makeText(getApplicationContext(), R.string.no_ticket, Toast.LENGTH_SHORT);
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
                    List<String> list = new ArrayList<>();
                    final BusSearchIinfoAdapter adapter = new BusSearchIinfoAdapter(getApplicationContext(), busList);
                    listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    listView.setAdapter(adapter);
                    adapter.setClickListener(new BusSearchIinfoAdapter.ItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) throws ParseException {

                            ///
                            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date value = formatter.parse( busList.get(position).getDepartureDate());
                            TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
                            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK); //this format changeable
                            dateFormatter.setTimeZone(timeZone);
                            String tt = dateFormatter.format(value);
                            //

                            Intent intent = new Intent(getApplicationContext(), BusTicketInfo.class);
                            intent.putExtra("busID", busList.get(position).getID());
                            intent.putExtra("fromDate", fromDate);
                            intent.putExtra("fromName", fromName);
                            intent.putExtra("toName", toName);
                            intent.putExtra("time", tt);
                            intent.putExtra("sourceID", sourceID);
                            intent.putExtra("destinationID", destinationID);
                            startActivity(intent);
                        }
                    });
                } catch (Exception ex) {
                    String d = ex.toString();
                    Log.e("errorrrrrrrrr", ex.toString());
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }
    public void backClicked(View v){
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }
}
