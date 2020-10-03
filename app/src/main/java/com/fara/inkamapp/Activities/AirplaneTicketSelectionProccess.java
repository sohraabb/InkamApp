package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.BusSeateSelectionAdapter;
import com.fara.inkamapp.BottomSheetFragments.AddNewAirplanePassenger;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.Fragments.AddAirplanPassenger;
import com.fara.inkamapp.Fragments.ChooseSeat;
import com.fara.inkamapp.Fragments.PassengerRecipte;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.AirplainModels.AirItinerary;
import com.fara.inkamapp.Models.AirplainModels.AirItinerary2;
import com.fara.inkamapp.Models.AirplainModels.AirReservationRequest;
import com.fara.inkamapp.Models.AirplainModels.AirTravelers;
import com.fara.inkamapp.Models.AirplainModels.FlightSegments;
import com.fara.inkamapp.Models.AirplainModels.PricedItineraries;
import com.fara.inkamapp.Models.AirplainModels.Telephone;
import com.fara.inkamapp.Models.AirplainModels.TravelerInfo;
import com.fara.inkamapp.Models.BusContact;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class AirplaneTicketSelectionProccess extends AppCompatActivity implements AddNewAirplanePassenger.NewPassengerListener {
    private RelativeLayout rl_services_oval, rl_receipt_oval;
    private ImageView iv_services, iv_receipt;
    private String token, userID, encryptedToken, AesKey, dataToConfirm, destinationID, sourceID, userName, toCity, fromCity, rph;
    private SharedPreferences sharedpreferences;
    double amount;
    private List<String> titles;
    private TextView toastText, title;
    private String busID, orderId;
    private List<Fragment> fragments;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private ViewPager pager;
    private PassengerListNotifyListener mListener;
    private TravelerInfo passengers;
    private BusSeateSelectionAdapter pageAdapter;
    private PricedItineraries request;

    @Override
    public void newPassenger(TravelerInfo passenger) {
        if (passenger == null) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.please_fil_info, Toast.LENGTH_SHORT);
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
            if (passengers == null) {
                passengers = new TravelerInfo();
                passengers.AirTravelers = new ArrayList<>();
                passengers.Telephone = passenger.Telephone;
                passengers.Email = passenger.Email;
                passengers.ClientUserTelephone = new Telephone();
                passengers.ClientUserTelephone.PhoneNumber = userName;
            }

            passengers.AirTravelers.add(passenger.AirTravelers.get(0));

            mListener.addToList(passenger.AirTravelers.get(0));
            switch (passenger.AirTravelers.get(0).PassengerType) {
                case "1":
                    amount += Double.parseDouble(request.airItineraryPricingInfo.ptcFareBreakdowns.get(0).passengerFares.get(0).originalFare.amount);
                    break;
                case "2":
                    amount += Double.parseDouble(request.airItineraryPricingInfo.ptcFareBreakdowns.get(1).passengerFares.get(0).originalFare.amount);
                    break;
                case "3":
                    amount += Double.parseDouble(request.airItineraryPricingInfo.ptcFareBreakdowns.get(2).passengerFares.get(0).originalFare.amount);
                    break;

            }
        }
    }

    public interface PassengerListNotifyListener {
        void addToList(AirTravelers passenger);
    }

    public void setOnPassengerListNotifyListener(PassengerListNotifyListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airplane_ticket_selection_proccess);

        titles = new ArrayList<>();
        titles.add(getString(R.string.choose_seat));
        titles.add(getString(R.string.passengers));
        titles.add(getString(R.string.travel_recipt));
        fragments = new ArrayList<>();


        toCity = getIntent().getStringExtra("toCity");
        fromCity = getIntent().getStringExtra("fromCity");

        String req = getIntent().getStringExtra("req");
        Gson g = new Gson();
        request = g.fromJson(req, PricedItineraries.class);
        rph = request.airItinerary.airItineraryRph;
        title = findViewById(R.id.tv_title2);


        Button btnContinue = findViewById(R.id.btn_continue);
        final LinearLayout llOne = findViewById(R.id.ll_line_1);

        rl_services_oval = findViewById(R.id.rl_services_oval);
        rl_receipt_oval = findViewById(R.id.rl_receipt_oval);
        iv_receipt = findViewById(R.id.iv_receipt);
        iv_services = findViewById(R.id.iv_services);
        btnContinue.setOnClickListener(view -> {

            switch (pager.getCurrentItem()) {

                case 0:
                    if (passengers != null && passengers.AirTravelers.size() == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.complete_passenger_info, Toast.LENGTH_SHORT);
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
                        BusTicketSelectionProcess.ResizeWidthAnimation a = new BusTicketSelectionProcess.ResizeWidthAnimation(llOne, ((View) llOne.getParent()).getWidth());
                        a.setDuration(500);
                        llOne.setVisibility(View.VISIBLE);
                        llOne.startAnimation(a);
                        a.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                rl_receipt_oval.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.green_oval_background));
                                iv_receipt.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_receipt_white));
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        // pager.setCurrentItem(pager.getCurrentItem() + 1, true);

                        fragments.add(new PassengerRecipte().newInstance(request.airItinerary.leaveOptions.get(0).flightSegments.get(0), passengers, toCity, fromCity,String.valueOf( amount)));
                        pageAdapter.notifyDataSetChanged();
                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    }
                    break;
                case 1:
                    // isSwiapeable = true;


                    try {

                        Bundle bundle = new Bundle();
                        AirReservationRequest req1 = new AirReservationRequest();
                        req1.AirItinerary = new AirItinerary2();
                        req1.AirItinerary.AirItinerary = new AirItinerary();
                        req1.AirItinerary.AirItinerary.airItineraryRph = rph;
                        req1.TravelerInfo = passengers;
                        req1.TravelerInfo.ClientUserTelephone.PhoneNumber=userName;
                        Gson g1 = new Gson();
                        bundle.putString("airplane", g1.toJson(req1));
                        bundle.putString("amount", String.valueOf(amount));
                        bundle.putInt("serviceType", 6);


                        bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                        bottomSheetDialogFragment.setArguments(bundle);
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    } catch (Exception e) {
                        Log.i("Income airplane", e.toString());
                    }

                    break;
            }


        });
        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(view -> {
            switch (pager.getCurrentItem()) {
                case 0:
                    finish();
                    break;
                case 1:
                    pager.setCurrentItem(0, true);
                    break;

            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        userName = sharedpreferences.getString("UserName", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        // fragments.add(new ChooseSeat().newInstance(getApplicationContext(), bus.getSeates()));
        fragments.add(new AddAirplanPassenger().newInstance(passengers == null ? new ArrayList<>() : passengers.AirTravelers));

        pageAdapter = new BusSeateSelectionAdapter(getSupportFragmentManager(), fragments);

        pager = findViewById(R.id.vp_progress);
        pager.setRotationY(180);
        pager.setAdapter(pageAdapter);
       /* pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return !isSwiapeable;
            }
        });*/

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(titles.get(position));
                if (pager.getCurrentItem() < position) {
                    // handle swipe LEFT
                } else if (pager.getCurrentItem() > position) {
                    // handle swipe RIGHT
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
