package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.AirplaneSearchInfoAdapter;
import com.fara.inkamapp.Adapters.BusSearchIinfoAdapter;
import com.fara.inkamapp.Adapters.CarouselPagerAdapter;
import com.fara.inkamapp.Fragments.ItemFragment;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.AirplainModels.AirTravelerAvailInfo;
import com.fara.inkamapp.Models.AirplainModels.AirTravelerAvails;
import com.fara.inkamapp.Models.AirplainModels.AvailableToken;
import com.fara.inkamapp.Models.AirplainModels.GetAirplaneTicketList;
import com.fara.inkamapp.Models.AirplainModels.LocationCode;
import com.fara.inkamapp.Models.AirplainModels.OriginDestinationInformation;
import com.fara.inkamapp.Models.AirplainModels.TicketRequest;
import com.fara.inkamapp.Models.AirplainModels.Travelpreferences;

import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class SearchTickets extends AppCompatActivity implements ItemFragment.SliderItemClickListener {

    private ImageButton back;
    private LinearLayout ticketInfo;
    private String token, userID, encryptedToken, AesKey, dataToConfirm, date, fromName, toName, flightType, returnDate, leaveClass, returnClass;
    private SharedPreferences sharedpreferences;
    private TextView toastText, tvCurrentLocation, tvDestination, tvDay, tvDate;
    private RecyclerView rv_tickets;
    private boolean isSelect;
    public CarouselPagerAdapter adapter;
    public ViewPager pager;
    public TextView selectedDay, selectedDate;
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

        setContentView(R.layout.activity_search_tickets);
        isSelect = false;
        tvCurrentLocation = findViewById(R.id.tv_current_location);
        tvDestination = findViewById(R.id.tv_destination);
        tvDay = findViewById(R.id.tv_day);
        tvDate = findViewById(R.id.tv_date);
        rv_tickets = findViewById(R.id.rv_tickets);
        pager = findViewById(R.id.rv_time_days);
        ticketInfo = findViewById(R.id.ll_ticket_info);
      /*  ticketInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AirplaneTicketInfo.class);
                startActivity(intent);
            }
        });*/

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException |
                InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        fromName = getIntent().getStringExtra("fromName");
        toName = getIntent().getStringExtra("toName");
        date = getIntent().getStringExtra("date");
        flightType = getIntent().getStringExtra("flightType");
        TicketRequest request = new TicketRequest();
        request.FlightType = "1";
        OriginDestinationInformation orgin = new OriginDestinationInformation();
        orgin.departureDate = date;
        LocationCode des = new LocationCode();
        des.code = to;
        LocationCode src = new LocationCode();
        src.code = from;
        orgin.destinationLocation = des;
        orgin.originLocation = src;
        tvCurrentLocation.setText(fromName);
        tvDestination.setText(toName);
        String[] separateDate = date.split("-");

        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tvDate.setText(Numbers.ToPersianNumbers(currentPersianDate));
        tvDay.setText(j.getPersianLongDate().split(" ")[0]);

        request.OriginDestinationInformation = new OriginDestinationInformation[]{orgin};
        request.TravelerInfoSummary = new AirTravelerAvails();
        AirTravelerAvailInfo traveler = new AirTravelerAvailInfo();
        traveler.PassengerType = "Adult";
        traveler.Quantity = 1;
        request.TravelerInfoSummary.AirTravelerAvail = new AirTravelerAvailInfo[]{traveler};
        Travelpreferences pref = new Travelpreferences();
        pref.CabinPref = new String[]{"All"};
        request.Travelpreferences = pref;
        Gson g = new Gson();
        dataToConfirm = g.toJson(request);
        new GetAvailableToken().execute();

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 3) * 2) + 60;
        pager.setPageMargin(-pageMargin);

        adapter = new CarouselPagerAdapter(this, getSupportFragmentManager(), date, 60);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(5);

        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClicked(String date, TextView dayOfWeek, TextView date1) {
        if (selectedDay != null) {
            selectedDate.setTextColor(Color.BLACK);
            selectedDay.setTextColor(Color.BLACK);
        }
        selectedDate = date1;
        selectedDay = dayOfWeek;
        this.date = date;
        String[] separateDate = this.date.replace("T", " ").split(" ")[0].split("-");

        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tvDate.setText(Numbers.ToPersianNumbers(currentPersianDate));
        tvDay.setText(j.getPersianLongDate().split(" ")[0]);
        Gson g = new Gson();
        TicketRequest req = g.fromJson(dataToConfirm, TicketRequest.class);
        req.OriginDestinationInformation[0].departureDate = date;
        dataToConfirm = g.toJson(req);
        new GetAvailableToken().execute();

    }

    private class GetAvailableToken extends AsyncTask<Void, Void, GetAirplaneTicketList> {

        GetAirplaneTicketList results = null;

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
        protected GetAirplaneTicketList doInBackground(Void... params) {
            results = new Caller().GetAvailableToken(userID, encryptedToken, dataToConfirm);

            return results;
        }

        @Override
        protected void onPostExecute(final GetAirplaneTicketList token) {
            super.onPostExecute(token);
            //TODO we should add other items here too
            if (token == null && !token.response.successful) {

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

                    final AirplaneSearchInfoAdapter adapter = new AirplaneSearchInfoAdapter(getApplicationContext(), token.data.pricedItineraries);
                    rv_tickets.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_tickets.setAdapter(adapter);
                    adapter.setClickListener(new AirplaneSearchInfoAdapter.ItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) throws ParseException {
                            Gson g = new Gson();
                            if (flightType.equals("1")) {
                                ///
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                                //Date value = formatter.parse( busList.get(position).getDepartureDate());
                                TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK); //this format changeable
                                dateFormatter.setTimeZone(timeZone);


                                Intent intent = new Intent(getApplicationContext(), AirplaneTicketInfo.class);


                                intent.putExtra("leaveClass", g.toJson(token.data.pricedItineraries.get(position)));
                                intent.putExtra("fromName", fromName);
                                intent.putExtra("toName", toName);
                                intent.putExtra("date", date);
                                intent.putExtra("flightType", flightType);
                                startActivity(intent);

                            } else if (!isSelect) {
                                leaveClass = g.toJson(token.data.pricedItineraries.get(position));

                                TicketRequest req = g.fromJson(dataToConfirm, TicketRequest.class);
                                req.OriginDestinationInformation[0].departureDate = returnDate;
                                String temp = "";
                                temp = req.OriginDestinationInformation[0].originLocation.code;
                                req.OriginDestinationInformation[0].originLocation.code = req.OriginDestinationInformation[0].destinationLocation.code;
                                req.OriginDestinationInformation[0].destinationLocation.code = temp;
                                dataToConfirm = g.toJson(req);
                                isSelect = true;
                                new AlertDialog.Builder(getApplicationContext())

                                        .setMessage("لطفا بلیط پرواز برگشت خود را انتخاب کنید.")

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                token.data.pricedItineraries.clear();
                                adapter.notifyDataSetChanged();
                                new GetAvailableToken().execute();
                            } else {
                                returnClass = g.toJson(token.data.pricedItineraries.get(position));
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                                //Date value = formatter.parse( busList.get(position).getDepartureDate());
                                TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK); //this format changeable
                                dateFormatter.setTimeZone(timeZone);


                                Intent intent = new Intent(getApplicationContext(), AirplaneTicketInfo.class);


                                intent.putExtra("leaveClass", leaveClass);
                                intent.putExtra("leaveClass", returnClass);
                                intent.putExtra("fromName", fromName);
                                intent.putExtra("toName", toName);
                                intent.putExtra("date", date);
                                intent.putExtra("returnDate", returnDate);
                                intent.putExtra("flightType", flightType);
                                startActivity(intent);
                            }
                        }
                    });

                } catch (Exception e) {
                    String g = e.toString();
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }
}
